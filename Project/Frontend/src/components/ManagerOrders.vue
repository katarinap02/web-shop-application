<template>
    
    <h1>Factory orders</h1>
    <div >

        <form v-bind:hidden="rejectClicked === 'NOT_CLICKED'">
            <table>
                <tr>
                    <td>Reason for rejection: </td>
                    <td><input type='text' name="comment" v-model="comment"></td>
                    <td><button type="submit" v-on:click="rejectOrder">Reject order</button></td>
                </tr>
            </table>
        </form>

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
                <td><button v-if="o.status === 'PENDING'" v-on:click="approveOrder(o.id)">Approve</button></td>
                <td><button v-if="o.status === 'PENDING'" v-on:click="rejectClick(o.id)">Reject</button></td>
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
const comment = ref("");
const rejectClicked = ref("NOT_CLICKED");
const rejectionId = ref("");
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

function rejectClick(orderId)
{
    rejectClicked.value = 'CLICKED';
    rejectionId.value = orderId;
}

function rejectOrder()
{
     axios.get("http://localhost:8080/WebShopAppREST/rest/buys/reject/?id=" + rejectionId.value + "&comment=" + comment.value)
     .then(response => { alert("Success!"); loadManagerOrders();});
}
function approveOrder(orderId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/approve/" + orderId)
    .then(response => { alert("Success!");

        loadManagerOrders();
    })
  
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