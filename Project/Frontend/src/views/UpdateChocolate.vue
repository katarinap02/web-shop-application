<template>
    <main>
      <div>
        <Navbar/>
    </div>
    <div class="table-container">
      <UpdateChocolateForm/>
    </div>
       
    </main>
    </template>
    
    <script setup>
    import Navbar from '@/components/basic-components/Navbar.vue';
    import UpdateChocolateForm from '../components/UpdateChocolateForm.vue';
    </script>

    <style scoped>
   .table-container{
    display: flex;
    flex-direction: column;
    margin-top: 20px;
    align-items: center;
    
   }
</style>
    