<template>
<div class="factory-info">
    <h1>{{ factory.name }}</h1>
    <img :src="factory.logoUrl" alt="Factory Logo" class="factory-logo" >
   </div>
    <div>
        <table class="info">
            <tr>
                <td class="label">Location: </td>
                <td><p>{{factory.location.latitude }} {{factory.location.longitude }} {{factory.location.address}}</p></td>
            </tr>
            <tr>
                <td class="label">Rate: </td>
                <td><p>{{ factory.rate !== -1 ? factory.rate : '' }}</p></td>
            </tr>
            <tr>
                <td class="label">Status: </td>
                <td :id="factory.isWorking ? 'working' : 'not working'"><p>{{ factory.isWorking ? 'working' : 'not working' }}</p></td>
            </tr>
            <tr>
                <td class="label"> Working time: </td>
                <td><p>{{ factory.workingHours.startHour }} - {{ factory.workingHours.endHour }}</p> </td>
            </tr>
        </table>
<div>
        <h1>Buys</h1>

        <table id="orders" class="tabela">
            <tr>
                <th>Order id </th>
                <th>Factory </th>
                <th>Date </th>
                <th>Status </th>
                <th>Price </th>

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
        <table id="customers" class="tabela">
            <tr>
                <th>Username </th>
                <th>Name </th>
                <th>Surname </th>
                <th>Points </th>
               

            </tr>

            <tr  v-for="c in customers" :key="c.id">
               <td>{{ c.username }}</td>
                <td>{{ c.name }}</td>
                <td>{{ c.surname }}</td>
                <td>{{ c.points }}</td>
               
              
            </tr>

           
        </table>
</div>
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



.factory-info {
  display: inline-block;
  vertical-align: middle; /* Aligns the inline-block elements vertically */
  margin-right: 20px; /* Adjust spacing between elements */
}

.factory-info h1 {
  display: inline-block; /* Ensures the h1 behaves as an inline-block element */
  margin: 0; /* Removes default margin */
  vertical-align: middle; /* Aligns vertically within the .factory-info container */
  margin: 0 auto;
    color: #5a086a;
    padding: 0;
  background: white;
  margin-right: 20px;
  margin-bottom: 20px;
}



.factory-info img {
  width: 70px;
  height: 70px;
  vertical-align: middle; /* Aligns vertically within the .factory-info container */
}

.info{
    margin: 0 auto;
    max-width: 480px;
    background: white;
    text-align: left;
    border-radius: 10px;
    padding: 25px;
    margin-bottom: 20px;
}

.info .label {
    color: gray;
    display: inline-block;
    margin: 20px 0 15px;
    font-size: 0.95em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;

}

.info  p {
    padding: 5px 10px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #aeaeae;
    color: #787878;
    display: block;
    width: 320px;
    margin-left: 10px;
}

.tabela {
	color: #381d11;
	background-color: wheat;
	border-collapse: collapse;
    margin-bottom: 30px;
    
    
}

.title {
  flex-grow: 1; 
  text-align: center;
}

.tabela th{
    padding: 8px;
	text-align: center;
	border-bottom: 1px solid #ddd;
    font-size: larger;
    background-color: #5a086a;
    color: antiquewhite;
    text-align: center;
    
}
.tabela td {
	padding: 8px;
	text-align: center;
	border-bottom: 1px solid #ddd;
    font-size: larger;
}

.tabela .create {
    text-decoration: none;
    background-color: #5a086a;
    color: antiquewhite;
    margin-left: auto; 
}



.tabela tr:hover {
	background-color: beige;
}

.tabela tr:hover {
	background-color: beige;
}

.show-btn{
    background-color: #5a086a;
    border: #5a086a;
}
.btn:hover {
    background-color: #0056b3;
}
.press-btn{
    background-color: #5a086a;
    border: #5a086a;
    width: 130px;
    margin: 30px;
}

h1 {
  margin: 0; /* Removes default margin */
  vertical-align: middle; /* Aligns vertically within the .factory-info container */
  margin: 0 auto;
    color: #5a086a;
    padding: 0;
  background: white;
  margin-bottom: 10px;
}



</style>