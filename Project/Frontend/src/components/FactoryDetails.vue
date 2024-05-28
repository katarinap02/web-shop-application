<template>
    <h1>{{ factory.name }}</h1>
    <img :src="factory.logoUrl" alt="Factory Logo" style="width: 200px; height: 200px;">
   
    <div>
        <table>
            <tr>
                <td>Lokacija: </td>
                <td>{{factory.location.latitude }} {{factory.location.longitude }} {{factory.location.address}}</td>
            </tr>
            <tr>
                <td>Ocena: </td>
                <td>{{ factory.rate }}</td>
            </tr>
            <tr>
                <td>Status: </td>
                <td>{{ factory.isWorking }}</td>
            </tr>
            <tr>
                <td>Radno vreme: </td>
                <td>{{ factory.workingHours.startHour }} - {{ factory.workingHours.endHour }} </td>
            </tr>
        </table>

    </div>
    <br>
    
    <div>
        <h1>Cokolade</h1>
        <table id="tabelacokolada">
            <tr>
                <th class="red">Naziv</th>
                <th class="red">Cena</th>
                <th class="red">Vrsta</th>
                <th class="red">Tip</th>
                <th class="red">Gramaza</th>
                <th class="red">Opis</th>
                <th class="red">Slika</th>
                <th class="red">Kolicina</th>
                <th class="red">Ima na stanju</th>
            </tr>

            <tr v-for="c in chocolates" :key="c.id">
                <td class="red">{{ c.name }}</td>
                <td class="red">{{ c.price }}</td>
                <td class="red">{{ c.kind }}</td>
                <td class="red">{{ c.type }}</td>
                <td class="red">{{ c.grams }}</td>
                <td class="red">{{ c.description }}</td>
                <td class="red">{{ c.image }}</td>
                <td class="red">{{ c.number }}</td>
                <td class="red">{{ c.status }}</td>
                <td><button v-on:click="goToUpdateChocolate(c.id)">Izmeni</button></td>
            </tr>
        </table>
        <div class="buttons"> 
            <button class="add" type="button" @click.prevent="goToAdd()">Dodaj</button>
            <button class="delete" >Obrisi</button>
        </div>


    </div>

   
</template>
<script setup>

import axios from 'axios';
import { onMounted, ref } from 'vue';
import { useRouter, useRoute } from 'vue-router';


const route = useRoute();
const router = useRouter();
const factoryId = ref(route.params.id);
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

const chocolates = ref([]);

onMounted(() => {  getFactoryById(factoryId); loadChocolates(factoryId);})

function loadChocolates(factoryId)
{
    axios.get("http://localhost:8080/WebShopAppREST/rest/chocolates/getfactoryId/" + factoryId.value)
    .then(response => {chocolates.value = response.data; console.log(response.data);})
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

</script>
<style scoped>

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
</style>

