import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import Login from '../views/LoginView.vue';
import Register from '../views/RegisterView.vue';
import Factory from '../views/ChocolateFactoriesView.vue';
import AddChocolate from '../views/AddChocolate.vue';
import UpdateChocolate from '../views/UpdateChocolate.vue';
import ShowChocolateFactory from '../views/ShowChocolateFactory.vue'
import CreateFactory from '../views/CreateFactoryView.vue'
import AddToCartView from '../views/AddToCartView.vue';
import ShowCartView from '../views/ShoppingCartView.vue';
import EditAmountView from '../views/EditAmountView.vue';
import CustomerOrdersView from '../views/CustomerOrdersView.vue';
import ManagerOrdersView from '../views/ManagerOrdersView.vue';
import ViewProfileView from '../views/ViewProfileView.vue';
import ViewUsersView from '../views/ViewUsersView.vue';
import AllUsersView from '../views/AllUsersView.vue';
import MyFactoryView from '../views/MyFactoryView.vue';
import OrderDetailsView from '@/views/OrderDetailsView.vue';
const routes = [
  {
    path: "/",
    name: "factory",
    component: Factory,
  },
  {
    path: '/login',
    name: 'login',
    component: Login
  },
  {
    path: '/register',
    name: 'register',
    component: Register,
    props: route => ({ id: route.query.id })
  },
  {
    path: '/addchocolate/:id',
    name: 'AddChocolate',
    component: AddChocolate
  },
  {
    path: '/createFactory',
    name: 'CreateFactory',
    component: CreateFactory
  },
  {
    path: '/updatechocolate/:id',
    name: 'UpdateChocolate',
    component: UpdateChocolate
  },
  {
    path: '/showfactory/:id',
    name: 'ShowFactory',
    component: ShowChocolateFactory
  },
  {
    path: "/about",
    name: "about",
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () =>
      import(/* webpackChunkName: "about" */ "../views/AboutView.vue"),
  },
  {
    path: '/viewProfile',
    name: 'ViewProfile',
    component: ViewProfileView
  },
  {
    path: '/viewUsers',
    name: 'ViewUsers',
    component: ViewUsersView
  },
  {
     path: "/addtocart/:id/:cartId",
     name: "AddToCart",
     component: AddToCartView
  },
  {
    path: "/showcart/:cartid",
    name: "ShowCart",
    component: ShowCartView

  },
  {
    path: "/editamount/:id/:cartId/:amount",
    name: "EditAmount",
    component: EditAmountView
  },
  {
    path: "/myorders",
    name: "MyOrders",
    component: CustomerOrdersView
  },
  {
    path: "/factoryorders",
    name: "FactoryOrders",
    component: ManagerOrdersView
  },
  {
    path: "/allusers",
    name: "AllUsers",
    component: AllUsersView
  },
  {
    path: "/myfactory",
    name: "MyFactory",
    component: MyFactoryView
  },
  {
    path: "/orderdetails/:orderId",
    name: "OrderDetails",
    component: OrderDetailsView
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
