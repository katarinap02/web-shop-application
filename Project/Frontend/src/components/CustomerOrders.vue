<template>
    
    <h1>My orders</h1>
        
        <div class="tabela">
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
    
                <tr  v-for="o in orders" :key="o.id">
                   <td>{{ o.id }}</td>
                    <td>{{ o.factoryName }}</td>
                    <td>{{ getDate(o.dateTime) }}</td>
                    <td>{{ o.status }}</td>
                    <td>{{ o.price }}</td>
                  
                    <td><button v-on:click="viewOrderItems(o.id)" class="btn btn-success show-btn">View</button></td>
                    <td><button v-if="o.status === 'PENDING'" class="btn btn-success show-btn" v-on:click="cancelOrder(o)">Cancel</button>
                    <button v-else-if="o.status === 'APPROVED' && !isRated(o.id)" class="btn btn-success show-btn" v-on:click="commentClick(o)">Rate factory</button></td>
                </tr>
    
               
            </table>
         
        </div>
        <form v-bind:hidden="commentClicked === 'NOT_CLICKED'">
            <table class="forma2">
                <tr>
                    <td class="label">Rate factory: </td>
                    <td><select v-model="newComment.rate">
                        <option value="5">5</option>
                        <option value="4">4</option>
                        <option value="3">3</option>
                        <option value="2">2</option>
                        <option value="1">1</option>

                    </select></td>
                   
                </tr>
                <tr>
                    <td class="label">Comment: </td>
                    <td><input type='text' v-model="newComment.commentText"></td>
                </tr>
                <tr>
                    <td colspan="2"><button type='submit' class="btn btn-success press-btn" v-on:click="leaveComment()">Rate factory</button></td>
                </tr>
            </table>
        </form>
        <button class="btn btn-success press-btn" @click="showFunctions1()">View functions</button>
         <div class="function" v-if="showFunctions">

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
            <button type="submit" class="btn btn-success search-btn" @click.prevent="search()">Search</button>
          
            </div>


            </form>
           
    </div>
        <div class="sort">
            <h3>Sort</h3>
            <div class="container">
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
            <div class="container">
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
            <div class="container">
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
const factories = ref([]);

const sortFactoryName = ref('unordered');
const sortPrice = ref('unordered');
const sortDate = ref('unordered');

const searchStartPrice = ref("");
const searchEndPrice = ref("");

const searchStartDate = ref("");
const searchEndDate = ref("");
const searchFactoryName = ref("");

const commentClicked = ref("NOT_CLICKED");
const commentOrder = ref({id: "", chocolateIds: [], factoryId: -1, dateTime: "0001-01-01T00:00:00", price: 0, customerName: "", status: "PENDING", factoryName: "", rated: 0});
const newComment = ref({id: 0, buyerId: "", factoryId: 0, commentText: "", rate: 0, approved: 0, statusSet: 0, orderId: ""});
const rated = ref('FALSE');
const allComments = ref([]);
const showFunctions = ref(false);

onMounted(() => {
    loadUser();
  

})

onMounted(() => {loadAllComments();})


function loadAllComments()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/comments/")
    .then(response => { allComments.value = response.data;  })
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

function isRated(orderId)
{
    
    if(allComments.value.filter(x => x.orderId === orderId).length > 0)
    {  return true;  }
    else
      return false;
    
}

function showFunctions1()
{
    showFunctions.value = !showFunctions.value;
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

function commentClick(order)
{
    commentClicked.value = 'CLICKED';
    commentOrder.value = order;
    
}

function leaveComment()
{
    alert("uslo");
    axios.post("http://localhost:8080/WebShopAppREST/rest/comments/" + commentOrder.value.id, this.newComment)
    .then(response => { alert("Factory successfully rated!"); refreshOrders(); loadAllComments(); });
}

function refreshOrders()
{
   
    loadCustomerOrders();
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
           
            orders.value.sort((a, b) => a.factoryName.toLowerCase().localeCompare(b.factoryName.toLowerCase())); 
        }
        else if(sortFactoryName.value === 'descending')
        {
            orders.value.sort((a, b) => b.factoryName.toLowerCase().localeCompare(a.factoryName.toLowerCase()))
        }
}

function refresh()
{
    loadCustomerOrders();
    searchStartPrice.value = "";
    searchEndPrice.value = "";

    searchStartDate.value = "";
    searchEndDate.value = "";
    searchFactoryName.value = "";
}

function search()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/searchcustomer/?name=" + searchFactoryName.value + "&startprice=" + searchStartPrice.value + "&endprice=" + searchEndPrice.value + "&startdate=" + searchStartDate.value + "&enddate=" + searchEndDate.value + "&customername=" + user.value.username)
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
    margin-bottom: 20px;
    
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

.forma2 select {
    padding: 5px 10px;
    box-sizing: border-box;
    border: 1px solid #5a086a;
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