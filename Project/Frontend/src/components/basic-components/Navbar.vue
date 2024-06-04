<template>
    <nav class="navbar navbar-expand-lg bg-body-tertiary">
        <div class="container-fluid">
          <img src="/images/chocolateFactoryLogo.svg" id="logo"/>
          
          <a class="navbar-brand navbar-title" href="#">Chocolate factory</a>
          <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
          </button>
          <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
              <li class="nav-item">
                <a class="nav-link active" aria-current="page" href="#">Home</a>
              </li>
              <li class="nav-item">
                <a class="nav-link" href="#/register">Register</a>
              </li>
             
              <li class="nav-item">
                <a class="nav-link" href="#/login">Log in</a>
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

.search-btn
{
    background: antiquewhite;
    color:  #5a086a;
    font-weight: bold;
    border: antiquewhite;
}


#logo{
    height: 80px;
    
}
</style>