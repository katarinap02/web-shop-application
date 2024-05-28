<template>
    <div class="tabela">
        <table>
            <tr>
                <th colspan="4">Chocolate factories</th>
            </tr>
            <tr>
                <th>Logo</th>
                <th>Name</th>
                <th>Location</th>
                <th>Average rating</th>
            </tr>
            <tr v-for="b in factories">
                <td>
            <img :src="b.logoUrl" alt="Factory Logo" style="width: 50px; height: 50px;">
          </td>
                <td>{{b.name}}</td>
                <td>{{b.location.latitude }} {{b.location.longitude }} {{b.location.address}}</td>
                <td>{{b.rate}}</td>

                <td><button v-on:click="showFactory(b.id)">Prikazi</button></td>

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

.tabela table {
    border: 1px solid black;
    margin: auto;
}

.tabela th, td {
    border: 1px solid black;
   width: auto;
}

.tabela td {
    text-align: left;
}




</style>