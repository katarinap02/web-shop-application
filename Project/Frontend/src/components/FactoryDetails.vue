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

    </div>
    <br>
    
    <div>
        <h1>Chocolates</h1>
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
            <th class="red">In Stock</th>
        </tr>


            <tr v-for="c in chocolates" :key="c.id" :class="{ selected: selectedChocolate && selectedChocolate.id === c.id }" @click="selectChocolate(c)" >
                <td class="red">{{ c.name }}</td>
                <td class="red">{{ c.price }}</td>
                <td class="red">{{ c.kind }}</td>
                <td class="red">{{ c.type }}</td>
                <td class="red">{{ c.grams }}</td>
                <td class="red">{{ c.description }}</td>
                <td>
            <img :src="c.imageUrl" alt="ChocolateImage" style="width: 50px; height: 50px;">
          </td>
                <td class="red">{{ c.number }}</td>
                <td class="red" :id="c.status ? 'yes' : 'no'">{{ c.status ? 'yes' : 'no' }}</td>
                <td><button v-if="user.role==='MANAGER' && user.factory.id === factory.id" v-on:click="goToUpdateChocolate(c.id)">Edit</button></td>
                
              
                <td><button v-if="user.role==='CUSTOMER'" v-on:click="addToCart(c.id)">Add to cart</button></td>
             
            </tr>
        </table>
        <div class="buttons"> 
<<<<<<< HEAD
            <button v-if="user.role==='MANAGER' && user.factory.id === factory.id" class="add" type="button" @click.prevent="goToAdd()">Add</button>
            <button v-if="user.role==='MANAGER' && user.factory.id === factory.id" class="delete" @click="deleteSelectedChocolate()">Delete selected</button>
            <button v-if="user.role==='CUSTOMER'" class="delete" @click="deleteSelectedChocolate()">View cart</button>
=======
            <button v-if="user.role==='MANAGER'" class="add" type="button" @click.prevent="goToAdd()">Add</button>
            <button v-if="user.role==='MANAGER'" class="delete" @click="deleteSelectedChocolate()">Delete selected</button>
            <button v-if="user.role==='CUSTOMER'" class="delete" @click="viewCart()">View cart</button>
>>>>>>> b5455264cef722a41d325a56cbfdc1ae53632562
        </div>

        <div class="comments-div">
            <h1>Comments</h1>
            <table class="comments">
                <tr>
                    <th>Comment</th>
                    <th>Rate</th>
                </tr>
                <tr v-for="c in comments" :key="c">
                    <td>{{ c.commentText }}</td>
                    <td>{{ c.rate }}</td>

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

const route = useRoute();
const router = useRouter();
const factoryId = ref(route.params.id);
const selectedChocolate = ref(null);
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
const chocolates = ref([]);
const comments = ref([]);

onMounted(() => {  getFactoryById(factoryId);  loadChocolates(factoryId); loadComments(factoryId)})

function loadChocolates(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getfactoryId/" + factoryId.value)
    .then(response => {chocolates.value = response.data; console.log(response.data);})
}

function loadComments(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/comments/" + factoryId.value)
    .then(response => {comments.value = response.data; console.log(response.data);})

}
function getFactoryById(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/factories/getid/" + factoryId.value)
     .then(response => { factory.value = response.data; console.log(response.data)});
}

function goToUpdateChocolate(chocolateId)
{
    this.router.push({ name: 'UpdateChocolate', params: { id: chocolateId } });
}

function goToAdd()
{
    this.router.push({ name: 'AddChocolate', params: { id: factoryId.value } });
}

function selectChocolate(chocolate)
{
    this.selectedChocolate = chocolate;
    console.log(this.selectedChocolate);
}
function openCart()
{
    
  
}

function addToCart(chocolateId)
{
      
      
    axios.get('http://localhost:8080/WebShopAppREST/rest/carts/' + user.value.username)
    .then(response => { shoppingCart.value = response.data;  this.router.push({name: 'AddToCart', params: {id: chocolateId, cartId: shoppingCart.value.id}}); console.log(response.data);})
    
   
    
    
    
    
}

function deleteSelectedChocolate()
{
    if(this.selectedChocolate != null)
    {
        axios.get('http://localhost:8080/WebShopAppREST/rest/chocolates/delete?id=' +  this.selectedChocolate.id).then(response =>
        {
            getFactoryById(factoryId);
            loadChocolates(factoryId);
            console.log(response.data);
        }
    )
        
    }
}

function viewCart()
{
    this.router.push({name: "ShowCart", params: {cartid: shoppingCart.value.id}});
}

</script>
<style scoped>

.comments{
    border: 1px solid black;
    width: 100%;
    text-align: center;
}

.comments th{
    border: 1px solid black;

}

.comments td{
    border-right: 1px solid black;;
}

#tabelacokolada{
    border: 1px solid black;
}

.red{
    border: 1px solid black;
}
div {
    display: flex;
    flex-direction: column;
    align-items: center;
}

.buttons {
    margin: 20px;
    display: inline;
}
.add {
    margin-right: 100px;
}

.selected {
    background-color: grey; /* Change to your desired highlight color */
}
</style>

