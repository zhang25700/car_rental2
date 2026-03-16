import Vue from "vue";
import Router from "vue-router";
import { getAccessToken } from "../utils/storage";

Vue.use(Router);

const router = new Router({
  mode: "hash",
  routes: [
    {
      path: "/login",
      component: () => import("../views/LoginPage.vue")
    },
    {
      path: "/",
      component: () => import("../layout/AppLayout.vue"),
      redirect: "/dashboard",
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
        }
      ]
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
  next();
});

export default router;
