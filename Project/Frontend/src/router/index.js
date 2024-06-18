import { createRouter, createWebHashHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import Login from '../views/LoginView.vue';
import Register from '../views/RegisterView.vue';
import Factory from '../views/ChocolateFactoriesView.vue';
import AddChocolate from '../views/AddChocolate.vue';
import UpdateChocolate from '../views/UpdateChocolate.vue';
import ShowChocolateFactory from '../views/ShowChocolateFactory.vue'
import CreateFactory from '../views/CreateFactoryView.vue'
import AddToCartView from '../views/AddToCartView.vue'
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
     path: "/addtocart/:id:cartId",
     name: "AddToCart",
     component: AddToCartView
  },
];

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
