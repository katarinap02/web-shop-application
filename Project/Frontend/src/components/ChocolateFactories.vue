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
            <tr v-for="b in factories" :key="b">
                <td>{{b.logoUrl}}</td>
                <td>{{b.name}}</td>
                <td>{{b.locationId}}</td>
                <td>{{b.Rate}}</td>
            </tr>
        </table>
    </div>
</template>

<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const factories = ref([]);
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
            console.log(response.data)
        }
        else {
            factories.value = [];
        }
    })
}





</script>

<style scoped>

.tabela table {
    border: 1px solid black;
    margin: auto;
}

.tabela th, td {
    border: 1px solid black;
    max-width: 300px; 
}

.tabela td {
    text-align: left;
}



</style>