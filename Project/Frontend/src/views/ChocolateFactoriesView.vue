<script setup>
import Factory from '@/components/ChocolateFactories.vue';
import Navbar from '@/components/basic-components/Navbar.vue';
</script>

<template>
<main>
    <div>
        <Navbar/>
    </div>
    
    <div class="table-container">
        <Factory/>
    </div>
    
</main>
    
</template>

<style scoped>
   .table-container{
    display: flex;
    flex-direction: column;
    margin-top: 10px;
    align-items: center;
    
   }
</style>