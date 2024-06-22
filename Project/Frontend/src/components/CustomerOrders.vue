<template>
    
    <h1>My orders</h1>
     <div class = "container">

        <div class="search">
            <h3>Search</h3>
           
            <form>
                <div>
                    <label>Factory name: </label>
                    <input type="text" name="name" v-model="searchFactoryName" required>
                </div>
                <div>

                    <label>Start price: </label>
                   
                <input type="number" name="searchStartPrice"  v-model="searchStartPrice" required>
              
            <label>
                End price: 
                </label>
                <input type="number" name="searchEndPrice"  v-model="searchEndPrice" required>
                
            </div>
           
                <div>
                    <label> Start date: </label>
                   
                <input type="date" name="searchStartDate"  v-model="searchStartDate" required>
               
                
                <label> End date: </label>
                   
                <input type="date" name="searchEndDate"  v-model="searchEndDate" required>
           
                </div>
                
            
            <div class="btn-container">
            <button type="submit" class="btn btn-success press-btn1" @click.prevent="search()">Search</button>
          
            </div>


            </form>
           
    </div>
        <div class="sort">
            <h3>Sort</h3>
            <div>
                <label>Factory name: </label>
                <label>
            <input type="radio" name="sortFactoryName" value="ascending" v-model="sortFactoryName">
            Ascending
        </label>
        <label>
            <input type="radio" name="sortFactoryName" value="descending" v-model="sortFactoryName">
            Descending
        </label>
        <label>
            <input type="radio" name="sortFactoryName" value="unordered" v-model="sortFactoryName" >
            Unordered
        </label>
            </div>
            <div>
                <label>Price: </label>
                <label>
            <input type="radio" name="sortPrice" value="ascending" v-model="sortPrice">
            Ascending
        </label>
        <label>
            <input type="radio" name="sortPrice" value="descending" v-model="sortPrice">
            Descending
        </label>
        <label>
            <input type="radio" name="sortPrice" value="unordered" v-model="sortPrice">
            Unordered
        </label>
            </div>
            <div>
                <label>Date: </label>
                <label>
            <input type="radio" name="sortDate" value="ascending" v-model="sortDate">
            Ascending
        </label>
        <label>
            <input type="radio" name="sortDate" value="descending" v-model="sortDate">
            Descending
        </label>
        <label>
            <input type="radio" name="sortDate" value="unordered" v-model="sortDate">
            Unordered
        </label>
            </div>
            
        </div>
        <div class="btn-container">
        <button class="btn btn-success press-btn1" @click.prevent="sort()">Sort</button>
        <button class="btn btn-success press-btn1" @click.prevent="refresh()">Refresh</button>
        </div>
        
        <div class="table">
            <table>
                <tr>
                    <th>Order id </th>
                    <th>Factory </th>
                    <th>Date </th>
                    <th>Status </th>
                    <th>Price </th>
                    <th> </th>
                    <th> </th>
    
                </tr>
    
                <tr  v-for="o in orders">
                   <td>{{ o.id }}</td>
                    <td>{{ o.factoryName }}</td>
                    <td>{{ getDate(o.dateTime) }}</td>
                    <td>{{ o.status }}</td>
                    <td>{{ o.price }}</td>
                    <td><button v-on:click="viewOrderItems(o.id)">View</button></td>
                    <td><button v-if="o.status === 'PENDING'" v-on:click="cancelOrder(o)">Cancel</button></td>
                </tr>
    
               
            </table>
         
        </div>

     </div>
    

</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';


const user = ref('');
const usernameData = ref(localStorage.getItem('userData'));
const factories = ref([]);

const sortFactoryName = ref('unordered');
const sortPrice = ref('unordered');
const sortDate = ref('unordered');

const searchStartPrice = ref(0);
const searchEndPrice = ref(0);

const searchStartDate = ref("");
const searchEndDate = ref("");
const searchFactoryName = ref("");



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

onMounted(() => { loadCustomerOrders();  })

function loadCustomerOrders()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/customer/" + usernameData.value)
    .then(response => {  console.log(response.data); orders.value = response.data});
}

function viewOrderItems(orderId)
{
    alert("To be implemented");
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

function cancelOrder(order)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/cancel/" + order.id)
    .then(response => { alert("Success!");

    axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/increaseamount/" + order.id)
      .then(response => {alert("Increased amount"); })
      .catch(error => {alert("Nije uspelo");});
  

    axios.post("http://localhost:8080/WebShopAppREST/rest/decreasepoints/?username=" + user.value.username + "&price=" + order.price)
    .then(response => { alert("Points are decreased!"); 

    loadCustomerOrders();
}); });
}

function getFactoryName(factoryId)
{
       // alert(factories.value.find(factory => factory.id === factoryId).name);
       
       if(factories.value.find(factory => factory.id === factoryId).name)
          return factories.value.find(factory => factory.id === factoryId).name;
    
}

function sort()
{

         
       
         if(sortPrice.value === 'ascending')
        {
           
           
            orders.value.sort((a, b) => a.price - b.price); 
        }
        else if(sortPrice.value === 'descending')
        {
            orders.value.sort((a, b) => b.price - a.price)
        }

        if (sortDate.value === 'ascending') {
    orders.value.sort((a, b) => {
        const dateA = new Date(a.dateTime);
        const dateB = new Date(b.dateTime);
        return dateA - dateB;
    });
} else if (sortDate.value === 'descending') {
    orders.value.sort((a, b) => {
        const dateA = new Date(a.dateTime);
        const dateB = new Date(b.dateTime);
        return dateB - dateA;
    });

  
}

if(sortFactoryName.value === 'ascending')
        {
           
            orders.value.sort((a, b) => a.factoryName.localeCompare(b.factoryName)); 
        }
        else if(sortFactoryName.value === 'descending')
        {
            orders.value.sort((a, b) => b.factoryName.toLowerCase().localeCompare(a.factoryName.toLowerCase()))
        }
}

function refresh()
{
    loadCustomerOrders();
}

function search()
{

    
    
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/searchcustomer/?name=" + searchFactoryName.value + "&startprice=" + searchStartPrice.value + "&endprice=" + searchEndPrice.value + "&startdate=" + searchStartDate.value + "&enddate=" + searchEndDate.value + "&customername=" + user.value.username)
    .then(response => { orders.value = response.data; })
}

</script>
<style scoped>


table {
    border: 1px solid black;
   
    background-color: white;
}

.sort {
    font-family: Arial, sans-serif;
   
    text-align: left
}

.sort h3 {
    font-size: 24px;
   
}

.sort div {
    margin-bottom: 10px;
}

.sort label {
    display: inline-block;
   
}

.sort label:first-child {
    font-weight: bold;
   
}

.table{
    display: flex;
    flex-direction: column;
    align-items: center;
}



.sort label input[type="radio"] + label {
    font-weight: normal;
}


.press-btn1 {
    background-color: #5a086a;
    border: none;
    color: white;
    margin-right: 10px; /* Adjust as needed */
    width: 80px;
}



.container{
    display: flex;
    flex-direction: column;
    align-items: center;
}

</style>