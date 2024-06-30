<template>
<h1>Chocolate factories</h1>


    <div class="tabela">
       
        <table>
            <tr>
                <th>Logo</th>
                <th>Name</th>
                <th>Location</th>
                <th>Average rating</th>
                <th> </th>
            </tr>
            <tr v-for="b in factories" :key="b.id" :class="{ selected: selectedFactory && selectedFactory.id === b.id }" @click="selectFactory(b)">
                <td>
            <img :src="b.logoUrl" alt="Factory Logo" style="width: 50px; height: 50px;">
          </td>
                <td>{{b.name}}</td>
                <td> {{b.location.address}} {{b.location.latitude }} {{b.location.longitude }}</td>
                <td>{{ b.rate !== -1 ? b.rate : '' }}</td>

                <td><button class="btn btn-success show-btn" v-on:click="showFactory(b.id)">Show</button></td>

            </tr>
        </table>
        
    </div>
    <div>
    <button v-if="user.role === 'ADMINISTRATOR'" class="btn btn-success press-btn" @click.prevent="goToCreateFactory()">Create factory</button>
    <button v-if="user.role === 'ADMINISTRATOR'" class="btn btn-success press-btn" @click="deleteSelectedFactory()">Delete factory</button>
    <button class="btn btn-success press-btn" @click="showFunctions1()">View functions</button>
    </div>

    <div class="function" v-if="showFunctions">

    <div class="search">
        <h3>Search</h3>
       
        <form >
            <div>
                <label>Fatory name: </label>
                <input type="text" name="name" v-model="searchFactory" required>

                
                <label>Chocolate name:</label>
               
            <input type="text" name="searchChocolate"  v-model="searchChocolate" required>
          
            </div>
            <div>
                <label>
                    Country: 
                    </label>
                    <input type="text" name="searchCountry"  v-model="searchCountry" required>
                    

        <label>
            City: 
            </label>
            <input type="text" name="searchCity"  v-model="searchCity" required>
            
          
        </div>
        <div>
            <label>
                Average rating: 
                </label>
                <input type="text" name="searchRating"  v-model="searchRating" required>
                
        </div>
       
          
        
        <div class="search-btn-container">
        <button type="submit" class="btn btn-success search-btn" @click.prevent="search()">Search</button>
      
        </div>


        </form>
       
</div>

    
 <div class="sort">
        <h3>Sort</h3>
        <div class="container">
            <label>Factory name:</label>
            <label >
        <input type="radio" name="sortName" value="ascending" v-model="sortFactoryName">
        Ascending
    </label>
    <label>
        <input type="radio" name="sortName" value="descending" v-model="sortFactoryName">
        Descending
    </label>
    <label>
        <input type="radio" name="sortName" value="unordered" v-model="sortFactoryName" >
        Unordered
    </label>
        </div>
        <div class="container">
            <label>Location:</label>
            <label>
        <input type="radio" name="sortLocation" value="ascending" v-model="sortLocation">
        Ascending
    </label>
    <label>
        <input type="radio" name="sortLocation" value="descending" v-model="sortLocation">
        Descending
    </label>
    <label>
        <input type="radio" name="sortLocation" value="unordered" v-model="sortLocation">
        Unordered
    </label>
        </div>
        <div class="container">
            <label>Average rating:</label>
            <label>
        <input type="radio" name="sortGrade" value="ascending" v-model="sortRating">
        Ascending
    </label>
    <label>
        <input type="radio" name="sortGrade" value="descending" v-model="sortRating">
        Descending
    </label>
    <label>
        <input type="radio" name="sortGrade" value="unordered" v-model="sortRating">
        Unordered
    </label>
    
        </div>
        <div class="sortbtn-container">
    <button class="sortbtn btn-success press-btn1" @click.prevent="sort()">Sort</button>
    <button class="sortbtn btn-success press-btn1" @click.prevent="refresh()">Refresh</button>
    </div>
        
    </div>
    

    <div class="filter">
        <h3>Filter</h3>
        <label for="combo1">Kind of chocolate:</label>
    <select id="combo1" name="combo1" v-model="kindFilter">
        <option value="all">All</option>
        <option value="ordinary">Ordinary</option>
        <option value="for eating">For eating</option>
        <option value="for cooking">For cooking</option>
        <option value="for drinking">For drinking</option>
    </select><br>

    <label for="combo2">Type of chocolate:</label>
    <select id="combo2" name="combo2" v-model="typeFilter">
        <option value="all">All</option>
        <option value="dark">Dark</option>
        <option value="white">White</option>
        <option value="milk">Milk</option>
        <option value="mixed">Mixed</option>
    </select><br>

    <label for="combo3">Opened:</label>
    <select id="combo3" name="combo3" v-model="opnenedFilter">
        <option value="all">All</option>
        <option value="true">Yes</option>
        <option value="false">No</option>
    </select><br>

    <button class="btn btn-success press-btn1" @click="filterChocolate()">Filter</button>

    

    </div>
</div>
   
    
   
</template>

<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const factories = ref([]);
const factoriesSort = ref('')
const factoriesAdress = ref({logo: "", name: "", location: "", average: ""})
const router = useRouter();
const user = ref('');
const carts = ref([]);
const usernameData = ref(localStorage.getItem('userData'));
const sortFactoryName = ref('unordered');
const sortLocation = ref('unordered');
const sortRating = ref('unordered');

const kindFilter = ref('all');
const typeFilter = ref('all');
const opnenedFilter = ref('all');
const chocolates = ref([]);
const copyFactory = ref([]);


const searchFactory = ref("");
const searchChocolate = ref("");
const searchCity = ref("");
const searchRating = ref("");

const searchCountry = ref("");
const showFunctions = ref(false);


function search()
{
    
    axios.get("http://localhost:8080/WebShopAppREST/rest/factories/search/?factory=" + searchFactory.value + "&chocolate=" + searchChocolate.value + "&location=" + searchCity.value + "&rating=" + searchRating.value + "&country=" + searchCountry.value)
    .then(response => { factories.value = response.data; 
        copyFactory.value = response.data;
        factories.value = copyFactory.value;
   const uniqueChocolates = new Set();
   const uniqueFactories = new Set();
    this.chocolates.forEach(chocolate => {
        let kindMatches = this.kindFilter === 'all' || chocolate.kind === this.kindFilter;
        let typeMatches = this.typeFilter === 'all' || chocolate.type === this.typeFilter; 
        if (kindMatches && typeMatches) {
            uniqueChocolates.add(chocolate.factory);
        }
    });

  
    this.factories.forEach(factory => {
                    console.log(factory.isWorking);
            console.log(this.opnenedFilter);
        if (uniqueChocolates.has(factory.id) && (this.opnenedFilter === 'all' || booleanToString(factory.isWorking) === this.opnenedFilter)) {
            uniqueFactories.add(factory);
        }
        else if(factoryHasNoChocolate(factory) && !factory.deleted && this.kindFilter === 'all'&& this.typeFilter === 'all' && (this.opnenedFilter === 'all' || booleanToString(factory.isWorking) === this.opnenedFilter))
        {
            uniqueFactories.add(factory);
        }
    });

    // Update the factories list
    factories.value = Array.from(uniqueFactories);





    })
}

function showFunctions1()
{
    showFunctions.value = !showFunctions.value;
}

function loadUser(){
  axios.get("http://localhost:8080/WebShopAppREST/rest/getLogedUser?username=" + usernameData.value)
    .then(response => {
        user.value = response.data;
        
        
    })
    .catch(error => {
      
    });

}




onMounted(() => {loadUser();})
const selectedFactory = ref(null);

onMounted(() => {
    loadFactories();
    loadChocolates();
    resetCarts();
   

})


function resetCarts()
{
    
    axios.get('http://localhost:8080/WebShopAppREST/rest/carts/emptycarts/?username=' + usernameData.value)
    .then(response => {console.log("uspesno");})
}




function loadFactories()
{
   

    axios.get('http://localhost:8080/WebShopAppREST/rest/factories').then(response => {
        if(response.data != "")
        {
            factories.value = response.data;
            copyFactory.value = response.data;
        }
        else {
            factories.value = '';
        }
        usernameData.value = localStorage.getItem('userData');
        loadUser();
    })
}

function loadChocolates()
{
    axios.get('http://localhost:8080/WebShopAppREST/rest/chocolates').then(response => {
        if(response.data != "")
        {
            chocolates.value = response.data;
        }
        else {
            chocolates.value = '';
        }
    })
}


function showFactory(factory)
{
   
        this.router.push({ name: 'ShowFactory', params: { id: factory } });
   
    
}

function goToCreateFactory()
{
    this.router.push({ name: 'CreateFactory' });
}

function selectFactory(factory)
{
    this.selectedFactory = factory;
    console.log(this.selectedFactory);
}

function deleteSelectedFactory()
{
    if(this.selectedFactory != null)
    {
        axios.get('http://localhost:8080/WebShopAppREST/rest/factories/delete?id=' +  this.selectedFactory.id).then(response =>
        {
            loadFactories();
            console.log(response.data);
        }
    )
        
    }
}

//---------------------------OVDE KRECE SORT-----------------------------------------

function sort()
{

    const getThirdWord = (address) => {
    const commaIndex = address.indexOf(',');
    if (commaIndex !== -1) {
        const partAfterComma = address.substring(commaIndex + 1).trim();
        const words = partAfterComma.split(' ');
        return words.length > 1 ? words[1] : ''; // Get the second word after the comma
    }
    return ''; // Return an empty string if no comma is found
};

if (sortLocation.value === 'ascending') {
    factories.value.sort((a, b) => {
        const wordA = getThirdWord(a.location.address).toLowerCase();
        const wordB = getThirdWord(b.location.address).toLowerCase();
        console.log(wordA);
        console.log(wordB);
        return wordA.localeCompare(wordB);
    });
}
else if(sortLocation.value === 'descending')
{
    factories.value.sort((a, b) => {
        const wordA = getThirdWord(a.location.address).toLowerCase();
        const wordB = getThirdWord(b.location.address).toLowerCase();
        return wordB.localeCompare(wordA);
    });
}

         if(sortFactoryName.value === 'ascending')
        {
            factories.value.sort((a, b) => a.name.localeCompare(b.name)); 
        }
        else if(sortFactoryName.value === 'descending')
        {
            factories.value.sort((a, b) => b.name.toLowerCase().localeCompare(a.name.toLowerCase()))
        }

        if(sortRating.value === 'ascending')
        {
           
           
            factories.value.sort((a, b) => a.rate - b.rate); 
        }
        else if(sortRating.value === 'descending')
        {
            factories.value.sort((a, b) => b.rate - a.rate)
        }
}

function refresh()
{
    loadFactories();
}

function filterChocolate()
{
    factories.value = copyFactory.value;
   const uniqueChocolates = new Set();
   const uniqueFactories = new Set();
    this.chocolates.forEach(chocolate => {
        let kindMatches = this.kindFilter === 'all' || chocolate.kind === this.kindFilter;
        let typeMatches = this.typeFilter === 'all' || chocolate.type === this.typeFilter; 

        if (kindMatches && typeMatches) {
            uniqueChocolates.add(chocolate.factory);
        }
    });

  
    this.factories.forEach(factory => {
                    console.log(factory.isWorking);
            console.log(this.opnenedFilter);
        if (uniqueChocolates.has(factory.id) && (this.opnenedFilter === 'all' || booleanToString(factory.isWorking) === this.opnenedFilter)) {
            uniqueFactories.add(factory);
        }
        else if(factoryHasNoChocolate(factory) && !factory.deleted &&  this.kindFilter === 'all'&& this.typeFilter === 'all' && (this.opnenedFilter === 'all' || booleanToString(factory.isWorking) === this.opnenedFilter))
        {
            uniqueFactories.add(factory);
        }
    });

    // Update the factories list
    factories.value = Array.from(uniqueFactories);


                 
 }


function booleanToString(boolean)
{
    if(boolean)
        return "true";
    else
        return "false";
}

function factoryHasNoChocolate(factoryCh)
{
    chocolates.value.forEach(chocolate => {
        if(chocolate.factory === factoryCh.id)
            return false;
    })
    return true;
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
	width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    width: 60%;
    
    
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

.tabela th {
	background-color: #5a086a;
    color: antiquewhite;
    text-align: center;
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

.selected {
    background-color: rgb(245, 195, 128); /* Change to your desired highlight color */
}

.sort {
    max-width: 560px;
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
    margin-top: 40px;
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

.search input[type="text"] {
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
}

.search-btn:hover {
    background-color: #0056b3;
}

.btn-text {
    margin-top: 5px; /* Adjust margin as needed */
    color: #555; /* Example color */
    font-size: 0.8em; /* Example font size */
}


.filter {
    max-width: 580px; /* Adjust width as needed */
    margin: 0 auto;
    padding: 20px;
    border: 1px solid wheat;
    border-radius: 8px;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    background-color: wheat;
    margin-bottom: 40px; /* Consistent margin with other sections */
    margin-left: 20px;
}

.filter h3 {
    font-size: 1.7em;
    margin-bottom: 10px;
    text-align: center; /* Center align the heading */
    color: #5a086a;
}

.filter .form-group {
    margin-bottom: 20px;
}

.filter label {
    display: inline-block;
    width: 150px; /* Adjust width to match the input/select */
    font-weight: bold;
    text-align: left;
    margin-bottom: 10px;
}

.filter select {
    padding: 8px;
    font-size: 1em;
    border: none;
    background: transparent;
    border: 1px solid gray;
    border-radius: 4px;
    width: calc(100% - 150px); /* Adjust width for better appearance */
    margin-bottom: 10px;
}

.filter .btn-container {
    text-align: center;
    margin-top: 15px;
}

.filter .btn {
    padding: 5px 10px;
    background-color: #5a086a;
    color: white;
    border: none;
    border-radius: 6px;
    cursor: pointer;
    margin-right: 10px; /* Adjust margin between buttons */
    display: inline-block;
    margin-top: 20px;
    width: 80px;
}

.filter .btn:hover {
    background-color: #0056b3;
}

.filter .form-group {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
}

.filter .form-group label {
    margin-right: 10px;
}

.filter .form-group select {
    flex: 1;
}

.map-container {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  margin-bottom: 20px;
}

#map {
  width: 10vh;
  height: 10vh;
}



</style>