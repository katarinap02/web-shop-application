<template>
    
    <h1>Factory orders</h1>
    <div >
        <table v-for="o in orders">
            <tr>
                <td>Order id: </td>
                <td>{{ o.id }}</td>
            </tr>

           
            <tr>
                <td>Status: </td>
                <td>{{ o.status }}</td>
            </tr>

            <tr>
                <td>Price: </td>
                <td>{{ o.price }}</td>
            </tr>
            
            <tr>
                <td><button v-on:click="viewOrderItems(o.id)">View</button></td>
                <td><button v-on:click="approveOrder(o.id)">Approve</button></td>
                <td><button v-on:click="rejectOrder(o.id)">Reject</button></td>
            </tr>
         

           
        </table>
     
    </div>

</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';


const user = ref('');
const usernameData = ref(localStorage.getItem('userData'));
const factoryId = ref(0);


onMounted(() => {
    loadUser();

})

function loadUser(){
    axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        user.value = response.data;
    })
    .catch(error => {
      //  localStorage.setItem('userData', JSON.stringify(""));
    });

}

const orders = ref([])

onMounted(() => { loadManagerOrders(); })


function loadFactoryId()
{
    alert(usernameData.value);
   axios.get("http://localhost:8080/WebShopAppREST/rest/getfactory/" + usernameData.value)
   .then(response => { alert("uslo"); console.log(response.data); factoryId.value = response.data; alert(factoryId.value);})
}
function loadManagerOrders()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/getfactory/" + usernameData.value)
   .then(response => { console.log(response.data);
    factoryId.value = response.data; 
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/manager/" + factoryId.value)
    .then(response => {  console.log(response.data); orders.value = response.data});

})
  
    
   
}

function viewOrderItems(orderId)
{
    alert("To be implemented");
}

function rejectOrder(orderId)
{
    alert("To be implemented");
}
function approveOrder(orderId)
{
    alert("To be implemented");
}

</script>
<style scoped>

div {
    display: flex;
    flex-direction: column;
    align-items: center;
}
table {
    border: 1px solid black;
    justify-items: left;
}
</style>