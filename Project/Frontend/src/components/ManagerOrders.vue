<template>
    
    <h1>Factory orders</h1>
        

        
        <div class="tabela">
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
                    <td>{{ formatStatus(o.status) }}</td>
                    <td>{{ o.price }}</td>
                    <td><button v-on:click="viewOrderItems(o.id)" class="btn btn-success show-btn">View</button></td>
                    <td><button v-if="o.status === 'PENDING'" v-on:click="approveOrder(o.id)" class="btn btn-success show-btn">Approve</button></td>
                    <td><button v-if="o.status === 'PENDING'" v-on:click="rejectClick(o.id)" class="btn btn-success show-btn">Reject</button></td>
                </tr>
    
               
            </table>
            
         
        </div>

        <form v-bind:hidden="rejectClicked === 'NOT_CLICKED'">
            <table class="forma2">
                <tr>
                    <td class="label">Reason for rejection: </td>
                    <td><input type='text' name="comment" v-model="comment"></td>
                    <td><button type="submit" class="btn btn-success press-btn" v-on:click="rejectOrder">Reject order</button></td>
                </tr>
            </table>
        </form>
        <button class="btn btn-success press-btn" @click="showFunctions1()">View functions</button>
        <div class="function" v-if="showFunctions">
    <div class="search">
        <h3>Search</h3>
        <form>
            <div>
                <label>Start price: </label>
                <input type="number" name="searchStartPrice" v-model="searchStartPrice" required>
                <label>End price: </label>
                <input type="number" name="searchEndPrice" v-model="searchEndPrice" required>
            </div>
            <div>
                <label>Start date: </label>
                <input type="date" name="searchStartDate" v-model="searchStartDate" required>
                <label>End date: </label>
                <input type="date" name="searchEndDate" v-model="searchEndDate" required>
            </div>
            <div class="btn-container">
                <button type="submit" class="btn btn-success search-btn" @click.prevent="search()">Search</button>
            </div>
        </form>
    </div>

    <div class="sort">
        <h3>Sort</h3>
        <div class="container">
            <label>Price: </label>
            <label>
                <input type="radio" name="sortPrice" value="ascending" v-model="sortPrice"> Ascending
            </label>
            <label>
                <input type="radio" name="sortPrice" value="descending" v-model="sortPrice"> Descending
            </label>
            <label>
                <input type="radio" name="sortPrice" value="unordered" v-model="sortPrice"> Unordered
            </label>
        </div>
        <div class="container">
            <label>Date: </label>
            <label>
                <input type="radio" name="sortDate" value="ascending" v-model="sortDate"> Ascending
            </label>
            <label>
                <input type="radio" name="sortDate" value="descending" v-model="sortDate"> Descending
            </label>
            <label>
                <input type="radio" name="sortDate" value="unordered" v-model="sortDate"> Unordered
            </label>
        </div>
        <div class="sortbtn-container">
            <button class="sortbtn btn-success press-btn1" @click.prevent="sort()">Sort</button>
            <button class="sortbtn btn-success press-btn1" @click.prevent="refresh()">Refresh</button>
        </div>
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

const searchStartPrice = ref("");
const searchEndPrice = ref("");

const searchStartDate = ref("");
const searchEndDate = ref("");
const searchFactoryName = ref("");
const showFunctions = ref(false);

const router = useRouter();


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
   .then(response => { console.log(response.data); factoryId.value = response.data; alert(factoryId.value);})
}
function loadManagerOrders()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/getfactory/" + usernameData.value)
   .then(response => { console.log(response.data);
    factoryId.value = response.data; 
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/manager/" + factoryId.value)
    .then(response => {  console.log(response.data); orders.value = response.data});

    searchStartPrice.value = "";
    searchEndPrice.value = "";

    searchStartDate.value = "";
    searchEndDate.value = "";
    searchFactoryName.value = "";

})
  
    
   
}

function formatStatus(input)
{
    return input.toLowerCase();
}

function showFunctions1()
{
    showFunctions.value = !showFunctions.value;
}

function viewOrderItems(orderId)
{
    this.router.push({name: "OrderDetails", params:{orderId: orderId}});
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

    console.log(searchFactoryName.value);
    console.log(searchEndDate.value);
    console.log(searchEndPrice.value);
    
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/searchmanager/?name=" + searchFactoryName.value + "&startprice=" + searchStartPrice.value + "&endprice=" + searchEndPrice.value + "&startdate=" + searchStartDate.value + "&enddate=" + searchEndDate.value + "&managername=" + user.value.username)
    .then(response => { orders.value = response.data; })
}

</script>
<style scoped>


template {
    height: 100%;
}

.tabela {
	color: #381d11;
	background-color: wheat;
	border-collapse: collapse;
	width: 65%;
    display: flex;
    flex-direction: column;
    
}

h1 {
    margin: 0 auto;
    color: #5a086a;
    padding: 0;
    background: white;
    max-width: 520px;
    margin-bottom: 20px;
}

.header-content {
  display: flex;
  justify-content: center;
  align-items: center;
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

.sort {
    max-width: 550px;
    margin: 0 auto;
    padding: 15px;
    border: 1px solid wheat;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color: wheat;
    margin-bottom: 40px;
    margin-left: 20px;
}

.sort h3 {
    font-size: 1.7em;
    margin-bottom: 10px;
    text-align: center; /* Center align the heading */
    color: #5a086a;
    
}

.sort-section {
    margin-top: 20px;
}

.container {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
}

.container label {
    display: inline-block;
    width: 130px; /* Adjust width as needed */
    font-weight: bold;
    text-align: left;
}

.container label:first-child {
    margin-right: 5px; /* Add margin-right to the first label */
}

.container label:not(:first-child) {
    margin-left: 10px; /* Add margin-left to subsequent labels */
}

.container input[type="radio"] {
    margin-right: 5px;
    margin-top: 20px;
}

.sort .sortbtn-container {
    text-align: center;
    margin-top: 30px;
    display: flex;
    margin-left: 180px;
}

.sort .sortbtn {
    padding: 5px 10px;
    background-color: #5a086a;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    margin-right: 20px; /* Adjust as needed */
    width: 80px;
}

.sort .sortbtn:hover {
    background-color: #0056b3;
}


.function{
    display: flex;
    justify-content: horizontal;
    max-height: 400px;
    margin-top: 20px;
   
}


.search {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid  wheat;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color:  wheat;
    margin-bottom: 40px;
}

.search h3 {
    font-size: 1.7em;
    color: #5a086a;
}

.search .form-group {
    margin-bottom: 20px;
}

.search label {
    display: inline-block;
    width: 130px; /* Adjust width as needed */
    font-weight: bold;
    text-align: left;
}

.search input {
    padding: 8px;
    font-size: 1em;
    border: none;
    background: transparent;
    border-bottom: 1px solid gray;
    border-radius: 4px;
    width: calc(100% - 130px); /* Adjust width for better appearance */
}

.search-btn-container {
    text-align: center;
    margin-top: 20px;
}

.search-btn {
    padding: 5px 10px;
    background-color: #5a086a;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    text-align: center; /* Center align text */
    display: inline-block; /* Ensure inline-block display to center text properly */
    margin-top: 20px;
}

.search-btn:hover {
    background-color: #0056b3;
}

.btn-text {
    margin-top: 5px; /* Adjust margin as needed */
    color: #555; /* Example color */
    font-size: 0.8em; /* Example font size */
}

.forma2 input {
    padding: 5px 10px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #5a086a;
    color: #555;
    display: block;
    width: 320px;
    margin-left: 10px;
}

.forma2 .label {
    color: gray;
    font-size: 0.95em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;

}

</style>