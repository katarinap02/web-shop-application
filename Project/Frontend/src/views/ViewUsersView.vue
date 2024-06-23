<script setup>
import ViewUsers from '@/components/ViewUsers.vue';
import Navbar from '@/components/basic-components/Navbar.vue';

</script>

<template>
<main>
    <div>
        <Navbar/>
    </div>
    <div class="table-container">
    <ViewUsers/>
    </div>
</main>
    
</template>

<style scoped>
   .table-container{
    display: flex;
    flex-direction: column;
    margin-top: 20px;
    align-items: center;
    
   }
</style>