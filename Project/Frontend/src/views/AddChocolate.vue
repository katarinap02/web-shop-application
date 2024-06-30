<template>
<main>
   <div>
        <Navbar/>
    </div>
    <div class="table-container">
   <ChocolateForm/>
   </div>
</main>
</template>

<script setup>

import ChocolateForm from '../components/ChocolateForm.vue';
import Navbar from '@/components/basic-components/Navbar.vue';
</script>

<style scoped>
   .table-container{
    display: flex;
    flex-direction: column;
    margin-top: 20px;
    align-items: center;
    
   }
</style>
