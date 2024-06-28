<template>
<div class="function">

    <div class="search">
        <h3>Search</h3>
       
        <form>
            <div>
                <label>Fatory name: </label>
                <input type="text" name="name" v-model="searchFactory" required>

                
                <label>Chocolate name: : </label>
               
            <input type="text" name="searchChocolate"  v-model="searchChocolate" required>
          
            </div>
            <div>
                <label>
                    Country: 
                    </label>
                    <input type="text" name="searchCountry"  v-model="searchCountry" required>
                    

        <label>
            Address: 
            </label>
            <input type="text" name="searchLocation"  v-model="searchLocation" required>
            
          
        </div>
        <div>
            <label>
                Average rating: 
                </label>
                <input type="text" name="searchRating"  v-model="searchRating" required>
                
        </div>
       
          
        
        <div class="btn-container">
        <button type="submit" class="btn btn-success press-btn1" @click.prevent="search()">Search</button>
      
        </div>


        </form>
       
</div>

    
 <div class="sort">
        <h3>Sort</h3>
        <div>
            <label>Factory name:</label>
            <label>
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
        <div>
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
        <div>
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
    <div class="btn-container">
    <button class="btn btn-success press-btn1" @click.prevent="sort()">Sort</button>
    <button class="btn btn-success press-btn1" @click.prevent="refresh()">Refresh</button>
    </div>
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
        <option value="option2">No</option>
    </select><br>

    <button class="btn btn-success press-btn1" @click="filterChocolate()">Filter</button>

    </div>
</div>

    <div class="tabela">
       
        <table>
            <tr>
                <th colspan="5">
            <div class="header-content">
              <span class="title">Chocolate factories</span>
            </div>
        </th>
            </tr>
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
                <td>{{b.location.latitude }} {{b.location.longitude }} {{b.location.address}}</td>
                <td>{{ b.rate !== -1 ? b.rate : '' }}</td>

                <td><button class="btn btn-success show-btn" v-on:click="showFactory(b.id)">Show</button></td>

            </tr>
        </table>
        
    </div>
    <div v-if="user.role === 'ADMINISTRATOR'">
    <button class="btn btn-success press-btn" @click.prevent="goToCreateFactory()">Create factory</button>
    <button class="btn btn-success press-btn" @click="deleteSelectedFactory()">Delete factory</button>
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
const searchLocation = ref("");
const searchRating = ref("");

const searchCountry = ref("");


function search()
{
    
    axios.get("http://localhost:8080/WebShopAppREST/rest/factories/search/?factory=" + searchFactory.value + "&chocolate=" + searchChocolate.value + "&location=" + searchLocation.value + "&rating=" + searchRating.value + "&country=" + searchCountry.value)
    .then(response => { factories.value = response.data; 
        copyFactory.value = response.data;
        factories.value = copyFactory.value;
   const uniqueChocolates = new Set();
   const uniqueFactories = new Set();
    this.chocolates.forEach(chocolate => {
        let kindMatches = this.kindFilter === 'all' || chocolate.kind === this.kindFilter;
        let typeMatches = this.typeFilter === 'all' || chocolate.type === this.typeFilter; 
        let openedMatches = this.opnenedFilter === 'all' || chocolate.opened === this.opnenedFilter; 

        if (kindMatches && typeMatches && openedMatches) {
            uniqueChocolates.add(chocolate.factory);
        }
    });

  
    this.factories.forEach(factory => {
        if (uniqueChocolates.has(factory.id)) {
            uniqueFactories.add(factory);
        }
    });

    // Update the factories list
    factories.value = Array.from(uniqueFactories);





    })
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
    const words = address.split(' ');
    return words.length >= 3 ? words[2] : '';
};

if (sortLocation.value === 'ascending') {
    factories.value.sort((a, b) => {
        const wordA = getThirdWord(a.location.address).toLowerCase();
        const wordB = getThirdWord(b.location.address).toLowerCase();
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
        let openedMatches = this.opnenedFilter === 'all' || chocolate.opened === this.opnenedFilter; 

        if (kindMatches && typeMatches && openedMatches) {
            uniqueChocolates.add(chocolate.factory);
        }
    });

  
    this.factories.forEach(factory => {
        if (uniqueChocolates.has(factory.id)) {
            uniqueFactories.add(factory);
        }
    });

    // Update the factories list
    factories.value = Array.from(uniqueFactories);


                 
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

.press-btn{
    background-color: #5a086a;
    border: #5a086a;
    width: 130px;
    margin: 30px;
}

.selected {
    background-color: rgb(245, 195, 128); /* Change to your desired highlight color */
}

.function .sort {
            font-family: Arial, sans-serif;
            margin: 20px;
            text-align: left
        }

        .sort h3 {
            font-size: 24px;
            margin-bottom: 10px;
            margin-left: 200px;
        }

        .filter h3 {
            font-size: 24px;
            margin-top: 30px;
            
        }

        .sort div {
            margin-bottom: 10px;
        }

        .sort label {
            display: inline-block;
            margin-right: 10px;
        }

        .sort label:first-child {
            font-weight: bold;
            margin-right: 15px;
        }

        .sort input[type="radio"] {
            margin-right: 5px;
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

        .btn-container {
            display: flex; /* Use flexbox for horizontal layout */
            height: 30px;
            
        }

        .container {
        display: flex;
        justify-content: space-between;
    }


.function{
    display: flex;
    justify-content: horizontal;
}

</style>