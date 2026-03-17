package com.example.carrental.service.impl;

import com.example.carrental.common.BusinessException;
import com.example.carrental.common.ErrorCode;
import com.example.carrental.context.UserContext;
import com.example.carrental.mapper.CustomerMapper;
import com.example.carrental.mapper.RentalOrderMapper;
import com.example.carrental.mapper.SysUserMapper;
import com.example.carrental.mapper.VehicleMapper;
import com.example.carrental.model.dto.OrderQueryRequest;
import com.example.carrental.model.dto.UserRentOrderRequest;
import com.example.carrental.model.entity.Customer;
import com.example.carrental.model.entity.RentalOrder;
import com.example.carrental.model.entity.SysUser;
import com.example.carrental.model.vo.PageResult;
import com.example.carrental.service.OrderService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
/**
 * 订单服务实现。
 * 覆盖后台订单查询、管理员创建订单、用户端下单，以及订单与车辆状态联动更新。
 */
public class OrderServiceImpl implements OrderService {

    private final CustomerMapper customerMapper;
    private final RentalOrderMapper rentalOrderMapper;
    private final SysUserMapper sysUserMapper;
    private final VehicleMapper vehicleMapper;

    /**
     * 后台订单分页查询。
     */
    @Override
    public PageResult<RentalOrder> page(OrderQueryRequest request) {
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<RentalOrder> list = rentalOrderMapper.selectByCondition(request);
        return PageResult.from(new PageInfo<>(list));
    }

    /**
     * 创建订单，并将对应车辆状态改为 RENTED。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(RentalOrder order) {
        order.setOrderNo(buildOrderNo());
        order.setCreatedBy(UserContext.getUserId());
        order.setOrderStatus(order.getOrderStatus() == null ? "CREATED" : order.getOrderStatus());
        rentalOrderMapper.insert(order);
        if (order.getVehicleId() != null) {
            vehicleMapper.updateStatus(order.getVehicleId(), "RENTED");
        }
    }

    /**
     * 用户端租车入口。
     * 将用户提交的租车表单转换成标准订单实体，再复用统一下单逻辑。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUserOrder(UserRentOrderRequest request) {
        Customer customer = ensureCustomerProfile();
        RentalOrder order = new RentalOrder();
        order.setCustomerId(customer.getId());
        order.setVehicleId(request.getVehicleId());
        order.setPickupTime(LocalDateTime.parse(request.getPickupTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.setReturnTime(LocalDateTime.parse(request.getReturnTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        order.setDailyRent(request.getDailyRent());
        order.setTotalAmount(request.getTotalAmount());
        order.setRemark(request.getRemark());
        create(order);
    }

    /**
     * 更新订单状态。
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(Long id, String orderStatus) {
        rentalOrderMapper.updateStatus(id, orderStatus);
    }

    /**
     * 确保当前登录用户有对应客户档案。
     * 如果历史数据缺失，则按当前账号自动补建一条客户信息。
     */
    private Customer ensureCustomerProfile() {
        String username = UserContext.get().getUsername();
        Customer customer = customerMapper.selectByPhone(username);
        if (customer != null) {
            return customer;
        }

        SysUser user = sysUserMapper.selectByUsername(username);
        if (user == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "当前用户不存在");
        }

        Customer newCustomer = new Customer();
        newCustomer.setCustomerName(user.getRealName() == null || user.getRealName().isBlank() ? username : user.getRealName());
        newCustomer.setPhone(username);
        newCustomer.setGender("未设置");
        newCustomer.setDriverLicenseNo("DL" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase());
        newCustomer.setIdCardNo("ID" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase());
        newCustomer.setStatus(1);
        customerMapper.insert(newCustomer);
        return newCustomer;
    }

    /**
     * 生成业务订单号。
     */
    private String buildOrderNo() {
        return "CR" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }
}
