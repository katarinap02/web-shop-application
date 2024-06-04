<template>
    <div class="tabela">
        <table>
            <tr>
                <th colspan="5">Chocolate factories</th>
            </tr>
            <tr>
                <th>Logo</th>
                <th>Name</th>
                <th>Location</th>
                <th>Average rating</th>
                <th> </th>
            </tr>
            <tr v-for="b in factories" :key="b.id">
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
</template>

<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const factories = ref([]);
const factoriesAdress = ref({logo: "", name: "", location: "", average: ""})
const router = useRouter();


onMounted(() => {
    loadFactories();

})

function loadFactories()
{
    axios.get('http://localhost:8080/WebShopAppREST/rest/factories').then(response => {
        if(response.data != "")
        {
            factories.value = response.data;
        }
        else {
            factories.value = [];
        }
    })
}

function showFactory(factory)
{
    this.router.push({ name: 'ShowFactory', params: { id: factory } });
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


.tabela th,
.tabela td {
	padding: 8px;
	text-align: center;
	border-bottom: 1px solid #ddd;
    font-size: larger;
}

.tabela th {
	background-color: #5a086a;
    color: antiquewhite;
    text-align: center;
}

.tabela tr:nth-child(even) {
	background-color: #444;
}

.tabela tr:hover {
	background-color: beige;
}

.show-btn{
    background-color: #5a086a;
    border: #5a086a;
}

</style>