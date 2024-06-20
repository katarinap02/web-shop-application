<template>
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

const factories = ref('');
const factoriesAdress = ref({logo: "", name: "", location: "", average: ""})
const router = useRouter();
const user = ref('');
const carts = ref([]);
const usernameData = ref(localStorage.getItem('userData'));



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
        }
        else {
            factories.value = '';
        }
        usernameData.value = localStorage.getItem('userData');
        loadUser();
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

</style>