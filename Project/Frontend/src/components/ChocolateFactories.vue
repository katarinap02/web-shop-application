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
            <tr v-for="b in factories"  :key="b">
                <td>
            <img :src="b.logoUrl" alt="Factory Logo" style="width: 50px; height: 50px;">
          </td>
                <td>{{b.name}}</td>
                <td>{{location.latitude }} {{location.longitude}} {{location.address}}</td>
                <td>{{b.rate}}</td>
                <td><button class="button" @click="loadLocation(b.id)">Show location</button></td>

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
const location = ref([]);
const locationAdd = ref("");
const pom = ref("");

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



function loadLocation(idLocation) {
        axios.get('http://localhost:8080/WebShopAppREST/rest/locations/id?location=' + idLocation)
            .then(response => {
                if (response.data !== "") {
                    console.log(response.data);
                    location.value = response.data;
                    console.log(location.value.id);
                    

                } else {
                    return "";
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
   width: auto;
}

.tabela td {
    text-align: left;
}




</style>