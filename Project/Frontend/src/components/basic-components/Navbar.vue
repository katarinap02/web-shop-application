<template>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
          <img src="/images/chocolateFactoryLogo.svg" id="logo"/>
          <div class="pom">
          <a class="navbar-brand navbar-title" href="#">Chocolate factory</a> <br>
          <h2 class="role" v-if="!user">UNREGISTERED</h2>
          <h2 class="role" v-else>{{ user.role }}</h2>
          </div>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <a class="nav-link active" aria-current="page" href="#">Home</a>
              </li>
              <li v-if="!user">
                  <a class="nav-link" href="#/register">Register</a>
              </li>
              <li v-else-if="user.role === 'ADMINISTRATOR'">
                  <a class="nav-link" href="#/register">Create Manager</a>
              </li>
              <li v-else-if="user.role === 'MANAGER'">
                  <a class="nav-link" href="#/register">Create Worker</a>
              </li>
             
              <li class="nav-item" v-if="!user">
                <a class="nav-link" href="#/login">Log in</a>
              </li>
              <li class="nav-item" v-else-if="user.role === 'ADMINISTRATOR' || 'MANAGER'">
                <a class="nav-link" href="#/" @click.prevent="logOut">Log out</a>
              </li>
            </ul>
            
            <form class="d-flex" role="search">
              <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
              <button class="btn btn-success search-btn" type="submit">Search</button>
            </form>
          </div>
        </div>
      </nav>
</template>
<script setup>
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const user = ref('');

onMounted(() => {
    loadUser();

})

function loadUser(){
    user.value = JSON.parse(localStorage.getItem('userData'));

}

function logOut()
{
    localStorage.setItem('userData', JSON.stringify(""));
    loadUser();
    window.location.reload();
}

</script>
<style scoped>

nav {
    background: #5a086a !important;
    
}
nav a:hover{
    color: beige !important;
    
}


.navbar-title {
    color: antiquewhite;
    font-size: x-large;
}


.nav-link{
    color: antiquewhite !important;
    font-size: x-large;
}

.role {
  color: antiquewhite !important;
  font-size: 12px;
}

.search-btn
{
    background: antiquewhite;
    color:  #5a086a;
    font-weight: bold;
    border: antiquewhite;
}

.pom {
  margin-top: 22px;
}


#logo{
    height: 80px;
    
}
</style>