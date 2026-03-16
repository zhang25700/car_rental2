import Vue from "vue";
import Router from "vue-router";
import { getAccessToken, getUserRole } from "../utils/storage";

Vue.use(Router);

const router = new Router({
  mode: "hash",
  routes: [
    {
      path: "/login",
      component: () => import("../views/LoginPage.vue")
    },
    {
      path: "/admin",
      component: () => import("../layout/AppLayout.vue"),
      redirect: "/admin/dashboard",
      meta: { roles: ["ADMIN", "OPERATOR", "STAFF"] },
      children: [
        {
          path: "dashboard",
          component: () => import("../views/DashboardPage.vue")
        },
        {
          path: "vehicles",
          component: () => import("../views/VehiclePage.vue")
        },
        {
          path: "customers",
          component: () => import("../views/CustomerPage.vue")
        },
        {
          path: "orders",
          component: () => import("../views/OrderPage.vue")
        },
        {
          path: "banners",
          component: () => import("../views/BannerPage.vue")
        },
        {
          path: "users",
          component: () => import("../views/UserPage.vue")
        }
      ]
    },
    {
      path: "/user",
      component: () => import("../layout/UserLayout.vue"),
      redirect: "/user/home",
      meta: { roles: ["USER"] },
      children: [
        {
          path: "home",
          component: () => import("../views/UserHomePage.vue")
        },
        {
          path: "vehicles",
          component: () => import("../views/UserVehiclePage.vue")
        },
        {
          path: "banners",
          component: () => import("../views/UserBannerPage.vue")
        }
      ]
    },
    {
      path: "/",
      redirect: () => (isAdminRole(getUserRole()) ? "/admin/dashboard" : "/user/home")
    }
  ]
});

router.beforeEach((to, from, next) => {
  if (to.path === "/login") {
    next();
    return;
  }

  if (!getAccessToken()) {
    next("/login");
    return;
  }

  const role = getUserRole();
  if (!role) {
    next("/login");
    return;
  }

  const allowedRoles = to.matched.flatMap(record => record.meta.roles || []);
  if (!allowedRoles.length || allowedRoles.includes(role)) {
    next();
    return;
  }

  next(isAdminRole(role) ? "/admin/dashboard" : "/user/home");
});

function isAdminRole(role) {
  return ["ADMIN", "OPERATOR", "STAFF"].includes(role);
}

export default router;
