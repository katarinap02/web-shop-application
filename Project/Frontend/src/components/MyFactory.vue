<template>
    <h1>{{ factory.name }}</h1>
    <img :src="factory.logoUrl" alt="Factory Logo" style="width: 200px; height: 200px;">
   
    <div>
        <table>
            <tr>
                <td>Location: </td>
                <td>{{factory.location.latitude }} {{factory.location.longitude }} {{factory.location.address}}</td>
            </tr>
            <tr>
                <td>Rate: </td>
                <td>{{ factory.rate !== -1 ? factory.rate : '' }}</td>
            </tr>
            <tr>
                <td>Status: </td>
                <td :id="factory.isWorking ? 'working' : 'not working'">{{ factory.isWorking ? 'working' : 'not working' }}</td>
            </tr>
            <tr>
                <td>Working time: </td>
                <td>{{ factory.workingHours.startHour }} - {{ factory.workingHours.endHour }} </td>
            </tr>
        </table>

        <h1>Buys</h1>

        <table id="orders">
            <tr>
                <th>Order id </th>
                <th>Factory </th>
                <th>Date </th>
                <th>Status </th>
                <th>Price </th>
                <th> </th>
                <th> </th>
                <th> </th>

            </tr>

            <tr  v-for="o in orders" :key="o.id">
               <td>{{ o.id }}</td>
                <td>{{ o.factoryName }}</td>
                <td>{{ getDate(o.dateTime) }}</td>
                <td>{{ o.status }}</td>
                <td>{{ o.price }}</td>
              
            </tr>

           
        </table>

        <h1>Customers</h1>
        <table id="customers">
            <tr>
                <th>Username </th>
                <th>Name </th>
                <th>Surname </th>
               

            </tr>

            <tr  v-for="c in customers" :key="c.id">
               <td>{{ c.username }}</td>
                <td>{{ c.name }}</td>
                <td>{{ c.surname }}</td>
               
              
            </tr>

           
        </table>

    </div>
</template>
<script setup>

import { onMounted, ref } from 'vue';
import axios from 'axios';
import { useRouter, useRoute } from 'vue-router';

const factory = ref({ "chocolates": [],
        "id": 0,
        "isWorking": false,
        "location": {
            "address": "",
            "id": 0,
            "latitude": 0,
            "longitude": 0
        },
        "logoUrl": "",
        "name": "",
        "rate": 0,
        "workingHours": {
            "endHour": "",
            "startHour": ""
        }});
const shoppingCart = ref({
    "chocolateIds": [
        -1
    ],
    "customerName": "",
    "id": 0,
    "price": 0.0,
    "isOpened": false,
    "factoryId": -1
});

const user = ref('');
const usernameData = ref(localStorage.getItem('userData'));
const orders = ref([]);
const customers = ref([]);

onMounted(() => {
    loadUser();

})



function loadUser(){
    axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        user.value = response.data;
        loadFactory(user.value.username)
    })
    .catch(error => {
      //  localStorage.setItem('userData', JSON.stringify(""));
    });

}


function loadFactory(username)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/factories/getfactoryobject/" + username)
    .then(response => { factory.value = response.data; 
        console.log(response.data);
        loadBuys(factory.value.id);
        loadCustomers(factory.value.id)
    });

}

function getDate(dateStr)
{
   const date = new Date(dateStr);

   const year = date.getFullYear();
   const month = ('0' + (date.getMonth() + 1)).slice(-2); 
    const day = ('0' + date.getDate()).slice(-2);
    const hours = ('0' + date.getHours()).slice(-2);
    const minutes = ('0' + date.getMinutes()).slice(-2);

    
    const formattedDate = `${year}-${month}-${day} ${hours}:${minutes}`;

    return formattedDate;
}

function loadCustomers(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/getcustomers/" + factoryId)
    .then(response => { customers.value = response.data;})

}
function loadBuys(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/managerapproved/" + factoryId)
    .then(response => { orders.value = response.data;       
    })
}
</script>

<style scoped>

div{
    display: flex;
    flex-direction: column;
    align-items: center;
}

#orders {
    border: 1px solid black;
    justify-items: left;
}

#customers {
    border: 1px solid black;
    justify-items: left;
}

</style>