<template>

  <div>

    <h1>Current cart</h1>
 
  <table class="tabela" id="tabelacokolada">
            <tr>
            <th class="red">Name</th>
            <th class="red">Price</th>
            <th class="red">Type</th>
            <th class="red">Category</th>
            <th class="red">Weight</th>
            <th class="red">Description</th>
            <th class="red">Image</th>
            <th class="red">Amount</th>
            <th></th>
           
        </tr>


            <tr v-for="c in items" :key="c.id" :class="{ selected: selectedChocolate && selectedChocolate.id === c.id }" @click="selectChocolate(c)" >
                <td class="red">{{ c.name }}</td>
                <td class="red">{{ c.price }}</td>
                <td class="red">{{ c.kind }}</td>
                <td class="red">{{ c.type }}</td>
                <td class="red">{{ c.grams }}</td>
                <td class="red">{{ c.description }}</td>
               
                <td>
            <img :src="c.imageUrl" alt="ChocolateImage" style="width: 50px; height: 50px;">
          </td>
          <td class="red">{{ getAmount(c.id) }}</td>
               <td><button class="btn btn-success edit-btn" v-on:click="editAmount(c.id)">Edit amount</button></td>
             
            </tr>
        </table>

        <table class="price">
          <td>Price: </td>
          <td>{{ shoppingCart.price }}</td>
        </table>

        <div class="btn-container">
          <button class="btn btn-success delete-btn" v-on:click="goBack()">View chocolates</button>
          <button class="btn btn-success order-btn" v-on:click="createOrder()">Create order</button>
          <button  class="btn btn-success delete-btn"  @click="deleteSelectedChocolate()">Delete selected</button>

        </div>
      
        

 

  </div>

  

</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

const router = useRouter();
const newOrder = ref({id: "", chocolateIds: [], factoryId: -1, dateTime: "0001-01-01T00:00:00", price: 0, customerName: "", status: "PENDING", factoryName: "", rated: 0});
const items = ref([]);
const selectedChocolate = ref(null);
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


onMounted(() => {
    loadUser();

})

function loadUser(){
    axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        user.value = response.data;
        console.log(user.value.role);
    })
    .catch(error => {
      //  localStorage.setItem('userData', JSON.stringify(""));
    });

}


onMounted(() => {loadCart(); })

function loadCart()
{
  console.log(usernameData.value);
   axios.get("http://localhost:8080/WebShopAppREST/rest/carts/" + usernameData.value)
   .then(response => {shoppingCart.value = response.data; console.log(response.data); loadItems();});

}

function loadItems()
{
 ;
  axios.get("http://localhost:8080/WebShopAppREST/rest/carts/getItems/" + shoppingCart.value.id)
  .then(response => { items.value = response.data; console.log(response.data);})
}

function selectChocolate(chocolate)
{
    this.selectedChocolate = chocolate;
    console.log(this.selectedChocolate);
}
function deleteSelectedChocolate()
{
    if(this.selectedChocolate != null)
    {
      var amount = this.shoppingCart.chocolateIds.filter(id => id === this.selectedChocolate.id).length;
        axios.delete("http://localhost:8080/WebShopAppREST/rest/carts/removeChocolate/?cartId=" + shoppingCart.value.id + "&chocolateId=" + this.selectedChocolate.id)
        .then(response => { shoppingCart.value = response.data; loadItems();})

        this.selectChocolate.amount = this.selectChocolate.amount + amount;
        axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + this.selectedChocolate.id, this.selectedChocolate)
      .then(response => { console.log(response.data);  });
        
    }
}

function editAmount(chocolateId)
{
  var amount = this.shoppingCart.chocolateIds.filter(id => id === chocolateId).length;
  this.router.push({name: 'EditAmount', params: {id: chocolateId, cartId: shoppingCart.value.id, amount: amount}}); 
}
function getAmount(chocolateId)
{
  return this.shoppingCart.chocolateIds.filter(id => id === chocolateId).length;
  
}

function goBack()
{
  this.router.push({name: 'ShowFactory', params: {id: shoppingCart.value.factoryId }});
}

function createOrder()
{
 
  if(this.shoppingCart.chocolateIds[0] === -1)
  {
    alert("You need to add items to cart.");
  }
  else
  {
   



   
    axios.get("http://localhost:8080/WebShopAppREST/rest/buys/" + user.value.username)
    .then(response => { alert("Success!");
  
   
    axios.post("http://localhost:8080/WebShopAppREST/rest/increasepoints/?username=" + user.value.username + "&price=" + this.shoppingCart.price)
    .then(response => { alert("Points are increased!"); 
     
    axios.get('http://localhost:8080/WebShopAppREST/rest/carts/emptycarts/?username=' + usernameData.value)
    .then(response => {console.log("uspesno");

   



    })


    });
    
      
    console.log(response.data);
    
  
  
  });

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

loadItems();
  }}


</script> 
<style scoped>




 div {
  display: flex;
  flex-direction: column;
  align-items: center;
 }

 .price {
  color: #5a086a;
  font-weight: bold;
  font-size: x-large;

 }

.tabela {
	color: #381d11;
	background-color: wheat;
	border-collapse: collapse;
    margin-bottom: 10px;
    
    
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

h1 {
  margin-top: 20px;
 
  vertical-align: middle;

    color: #5a086a;
 
  background: white;
  margin-bottom: 10px;
}

.title {
  flex-grow: 1; 
  text-align: center;
}

.delete-btn{
  background-color: #5a086a;
  border: #5a086a;
  width: 140px;
}
.btn:hover {
  background-color: #0056b3;
}
.order-btn{
  background-color: #5a086a;
  border: #5a086a;
  width: 135px;
  margin: 30px;
}

.edit-btn{
  background-color: #5a086a;
  border: #5a086a;
 
}

.btn-container{
  display: flex;
  flex-direction: row;
  margin-left: 40px;
}
</style>