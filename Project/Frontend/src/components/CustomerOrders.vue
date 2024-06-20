<template>
    
    <h1>My orders</h1>
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
                <td><button v-if="o.status === 'PENDING'" v-on:click="cancelOrder(o)">Cancel</button></td>
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

onMounted(() => { loadCustomerOrders();})

function loadCustomerOrders()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/customer/" + usernameData.value)
    .then(response => {  console.log(response.data); orders.value = response.data});
}

function viewOrderItems(orderId)
{
    alert("To be implemented");
}

function cancelOrder(order)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/cancel/" + order.id)
    .then(response => { alert("Success!");
  

    axios.post("http://localhost:8080/WebShopAppREST/rest/decreasepoints/?username=" + user.value.username + "&price=" + order.price)
    .then(response => { alert("Points are decreased!"); 
}); });
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