<template>
<div class="factory-info">
    <h1>{{ factory.name }}</h1>
    <img :src="factory.logoUrl" alt="Factory Logo" class="factory-logo" >
   </div>
   
    <div>
        <table class="info">
           
            <tr>
                <td class="label">Rate: </td>
                <td><p>{{ factory.rate !== 0 ? factory.rate : '' }}</p></td>
            </tr>
            <tr>
                <td class="label">Status: </td>
                <td :id="factory.isWorking ? 'working' : 'not working'"><p>{{ factory.isWorking ? 'working' : 'not working' }}</p></td>
            </tr>
            <tr>
                <td class="label">Working time: </td>
                <td><p>{{ factory.workingHours.startHour }} - {{ factory.workingHours.endHour }}</p> </td>
            </tr>

            <tr>
                <td class="label">Location: </td>
                <td><p>{{factory.location.latitude }} {{factory.location.longitude }} {{factory.location.address}}</p></td>
            </tr>
           
        </table>

    </div>
    <div>
        <button v-on:click="showMap()"  class="btn btn-success show-btn" >Show map</button>
        <div  v-show="mapClicked !== 'NO_CLICK'" id="map"></div>

    </div>
    <br>
    
    <div>
        <h1>Chocolates</h1>
        <table class="tabela">
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
            <th v-if="user.role==='CUSTOMER'"></th>
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
                
              
                <td v-if="user.role==='CUSTOMER'"><button class="btn btn-success show-btn" v-on:click="addToCart(c.id)">Add to cart</button></td>
             
            </tr>
        </table>
        <div class="buttons"> 
            <button v-if="user.role === 'MANAGER' && user.factory && user.factory.id === factory.id" class="btn btn-success press-btn" type="button" @click.prevent="goToAdd()">Add</button>
            <button v-if="user.role==='MANAGER' && user.factory.id === factory.id" class="btn btn-success press-btn" @click="goToUpdateChocolate()">Edit selected</button>
            <button v-if="user.role === 'MANAGER' && user.factory && user.factory.id === factory.id" class="btn btn-success press-btn" @click="deleteSelectedChocolate()">Delete selected</button>
            <button v-if="user.role === 'CUSTOMER'" class="btn btn-success press-btn" @click="viewCart()">View cart</button>
            <div v-if="selectedChocolate && user.factory && user.factory.id === factory.id && user.role === 'WORKER'" class="changeAmount">
                <input name="amount" type="number" v-model="chocolateAmount" :class="{'error': chocolateAmount < 0}">
                <button class="btn btn-success press-btn" type="button"  @click.prevent="changeChocolateAmount()">Change amount</button>
            </div>
        </div>

        
            <h1>Comments</h1>
            <table class="tabela">
                <tr>
                    <th>User</th>
                    <th>Comment</th>
                    <th>Rate</th>
                    <th v-if="user.role === 'MANAGER' "></th>
                    <th v-if="user.role === 'MANAGER' "></th>
                   
                </tr>
                <tr v-for="c in comments" :key="c">
                    <td>{{ c.buyerId }}</td>
                    <td>{{ c.commentText }}</td>
                    <td>{{ c.rate }}</td>
                  
                    <td v-if="user.role === 'MANAGER' && c.statusSet == 0"><button  class="btn btn-success show-btn" v-on:click="approveComment(c.id)">Approve</button></td>
                    <td v-if="user.role === 'MANAGER' && c.statusSet == 0"><button class="btn btn-success show-btn" v-on:click="rejectComment(c.id)">Reject</button></td>

                </tr>
            </table>

            

        </div>

   
</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import "ol/ol.css";
// This is library of openlayer for handle map
import Map from "ol/Map";
import View from "ol/View";
import { defaults as defaultControls, ScaleLine } from "ol/control";
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer';
import {OSM, Vector as VectorSource} from 'ol/source';

import { transformExtent, transform } from 'ol/proj';
import { buffer as olExtentBuffer } from 'ol/extent';
import { GeoJSON } from 'ol/format';
import Select from 'ol/interaction/Select';
import { click } from 'ol/events/condition';
import { fromLonLat, toLonLat } from 'ol/proj';
import Point from 'ol/geom/Point';
import Feature from 'ol/Feature';

const user = ref('');
const usernameData = ref(localStorage.getItem('userData'));
const serbiaExtent = transformExtent([18.817, 41.860, 23.006, 46.192], 'EPSG:4326', 'EPSG:3857');
const centerSerbia = transform([20.9115, 44.026], 'EPSG:4326', 'EPSG:3857');

const mapClicked = ref("NO_CLICK");
onMounted(() => {
    loadUser();

})

function showMap()
{
    mapClicked.value = ref("CLICKED");
   
    const bufferedExtent = olExtentBuffer(serbiaExtent, 100000);
    const center = transform([factory.value.location.longitude, factory.value.location.latitude], 'EPSG:4326', 'EPSG:3857');

  const map = new Map({
    target: 'map', 
    layers: [
      new TileLayer({
        source: new OSM() 
      })
    ],
    view: new View({
      center: center, 
      zoom: 12, 
      extent: bufferedExtent, 
      showFullExtent: true,
    })
  });
  const vectorSource = new VectorSource();
const vectorLayer = new VectorLayer({
    source: vectorSource,
});
map.addLayer(vectorLayer);


function addMarker(longitude, latitude) {
  
    vectorSource.clear();

  
    const coords = fromLonLat([longitude, latitude]);

   
    const marker = new Feature({
        geometry: new Point(coords)
    });

   
    vectorSource.addFeature(marker);
}


const factoryLongitude = factory.value.location.longitude;
const factoryLatitude = factory.value.location.latitude;
addMarker(factoryLongitude, factoryLatitude);
}

function loadUser(){
    axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        user.value = response.data;
        loadComments(factoryId);
    })
    .catch(error => {
      //  localStorage.setItem('userData', JSON.stringify(""));
    });

}

function approveComment(commentId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/comments/approve/" + commentId)
    .then(response => {
        alert("Success!");
        refreshComments();
        return axios.get('http://localhost:8080/WebShopAppREST/rest/factories/rate'); // Replace with your second request URL
    })
    .then(secondResponse => {
        // Handle the response from the second request here
        console.log("Second request success:", secondResponse.data);
        // Add any additional logic here
    })
    .catch(error => {
        // Handle errors for both requests here
        console.error("Error occurred:", error);
    });

}

function rejectComment(commentId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/comments/reject/" + commentId)
    .then(response => { alert("Success!"); refreshComments();});
}

const route = useRoute();
const router = useRouter();
const factoryId = ref(route.params.id);
const selectedChocolate = ref(null);
const selectedAmount = ref(null);
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
const chocolateAmount = ref('0');

onMounted(() => {  getFactoryById(factoryId);  loadChocolates(factoryId); })

function loadChocolates(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getfactoryId/" + factoryId.value)
    .then(response => {chocolates.value = response.data; 
    
        if(user.value.role === 'CUSTOMER')
      {
       
        chocolates.value = chocolates.value.filter(x => x.number > 0);
        console.log(chocolates.value);
      }


    })
   
  
}

function refreshComments()
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/comments/" + factoryId.value)
    .then(response => {comments.value = response.data; console.log(response.data);})
}

function loadComments(factoryId)
{
   
 
    if(user.value.role === "CUSTOMER" || user.value.role === "UNREGISTERED" || user.value.role === "WORKER")
    {
        loadApprovedComments(factoryId);
    }
    else
    {
        axios.get("http://localhost:8080/WebShopAppREST/rest/comments/" + factoryId.value)
        .then(response => {comments.value = response.data; console.log(response.data);})

    }
   

}

function loadApprovedComments(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/comments/" + factoryId.value)
    .then(response => {  comments.value = response.data.filter(x => x.approved == 1); console.log(response.data);})

}
function getFactoryById(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/factories/getid/" + factoryId.value)
     .then(response => { factory.value = response.data; console.log(response.data)});
}

function goToUpdateChocolate()
{
    if(this.selectedChocolate != null)
    {
        console.log(this.selectedChocolate);
        this.router.push({ name: 'UpdateChocolate', params: { id: this.selectedChocolate.id } });
    }
}

function goToAdd()
{
    this.router.push({ name: 'AddChocolate', params: { id: factoryId.value } });
}

function selectChocolate(chocolate)
{
    this.chocolateAmount = chocolate.number;
    this.selectedChocolate = chocolate;
    this.selectedAmount = chocolate;
}
function openCart()
{
    
  
}

function changeChocolateAmount()
{
    if(user.value.role==='WORKER'&& user.value.factory.id === factory.value.id && this.selectChocolate !== null)
    {
        selectedAmount.value.number = chocolateAmount.value;
        axios.post("http://localhost:8080/WebShopAppREST/rest/chocolates/" + selectedAmount.value.id, selectedAmount.value)
      .then(response => { console.log(response.data); 
       });
    }
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

div {
    display: flex;
    flex-direction: column;
    align-items: center;
}


.selected {
    background-color: grey; /* Change to your desired highlight color */
}

.map-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    
}
#map {
    width: 40vh;
    height: 40vh;
  }
.error{
      border: 2px solid red;
  }

.changeAmount {
    display: inline;
    align-items: center; 
}

.changeAmount input { 
    width: 50px;
    text-align: center;
    border: 1px solid gray;
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


.selected {
    background-color: rgb(245, 195, 128); /* Change to your desired highlight color */
}

.tabela {
	color: #381d11;
	background-color: wheat;
	border-collapse: collapse;
    margin-bottom: 10px;
    
    
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

.buttons {
  display: inline-block;
}

.buttons button {
  margin-right: 10px; /* Adjust spacing between buttons */
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
    width: 140px;
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

