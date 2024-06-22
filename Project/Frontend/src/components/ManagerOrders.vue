<template>
    
    <h1>Factory orders</h1>
    <div class="container">

        
        <div class="search">
            <h3>Search</h3>
           
            <form>
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

        <form v-bind:hidden="rejectClicked === 'NOT_CLICKED'">
            <table>
                <tr>
                    <td>Reason for rejection: </td>
                    <td><input type='text' name="comment" v-model="comment"></td>
                    <td><button type="submit" v-on:click="rejectOrder">Reject order</button></td>
                </tr>
            </table>
        </form>

        
        <div class="table">
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
    
                <tr  v-for="o in orders">
                   <td>{{ o.id }}</td>
                    <td>{{ o.factoryName }}</td>
                    <td>{{ getDate(o.dateTime) }}</td>
                    <td>{{ o.status }}</td>
                    <td>{{ o.price }}</td>
                    <td><button v-on:click="viewOrderItems(o.id)">View</button></td>
                    <td><button v-if="o.status === 'PENDING'" v-on:click="approveOrder(o.id)">Approve</button></td>
                    <td><button v-if="o.status === 'PENDING'" v-on:click="rejectClick(o.id)">Reject</button></td>
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
const factoryId = ref(0);
const comment = ref("");
const rejectClicked = ref("NOT_CLICKED");
const rejectionId = ref("");
const chocolates = ref([]);

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


function increaseChocolateAmount(orderId)
{
      axios.get("http://localhost:8080/WebShopAppREST/rest/buys/increaseamount/" + orderId)
      .then(response => {alert("Increased amount"); })
      .catch(error => {alert("Nije uspelo");});
}
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
     .then(response => { alert("Success!"); 
    axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/increaseamount/" + rejectionId.value)
      .then(response => {alert("Increased amount"); })
      .catch(error => {alert("Nije uspelo");});

      loadManagerOrders();
     });
}
function approveOrder(orderId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/approve/" + orderId)
    .then(response => { alert("Success!");

        loadManagerOrders();
    })
  
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


}

function refresh()
{
    loadManagerOrders();
}

function search()
{

    
    
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/searchmanager/?name=" + searchFactoryName.value + "&startprice=" + searchStartPrice.value + "&endprice=" + searchEndPrice.value + "&startdate=" + searchStartDate.value + "&enddate=" + searchEndDate.value + "&managername=" + user.value.username)
    .then(response => { orders.value = response.data; })
}

</script>
<style scoped>


table {
    border: 1px solid black;
    justify-items: left;
}

.container{
    display: flex;
    flex-direction: column;
    align-items: center;
}

#orders {
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