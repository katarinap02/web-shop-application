<template>
<h1>Create factory</h1>
  <form name="fabrikaForma" class="fabrikaForma">
    
    <table>
        <tr>
    <td class="label">Name:</td>
    <td><input name="name" type="text" v-model="factory.name" :class = "{'error':factoryValid.name === ''}"></td>
</tr>
<tr>
    <td class="label">Latitude:</td>
    <td><input name="latitude" type="text" v-model="factory.location.latitude" ></td>
</tr>
<tr>
    <td class="label">Longitude:</td>
    <td><input name="longitude" type="text" v-model="factory.location.longitude" ></td>
</tr>
<tr>
    <td class="label">Address:</td>
    <td><input name="address" type="text" v-model="factory.location.address" :class = "{'error':factoryValid.location.address === ''}"></td>
</tr>
<tr>
    <td>  </td>
    <td><button v-on:click="showMap()"  class="submit">Show map</button></td>
</tr>
<tr>
    <td class="label">Image:</td>
    <td><input name="image" type="text" v-model="factory.logoUrl" :class = "{'error':factoryValid.logoUrl === ''}"></td>
</tr>
<tr>
    <td class="label">Start Hour:</td>
    <td><input name="start" type="time" v-model="factory.workingHours.startHour" :class = "{'error':factoryValid.workingHours.startHour === ''}"></td>
</tr>
<tr>
    <td class="label">End Hour:</td>
    <td><input name="end" type="time" v-model="factory.workingHours.endHour" :class = "{'error':factoryValid.workingHours.endHour === ''}"></td>
</tr>
<tr>
    <td class="label">Maganer</td>
    <td v-if="managers">
        <select name="manager" id="manager" v-model="mainManager">
            <option v-for="manager in managers" :key="manager.username" :value="manager">
                {{ manager.name }} {{ manager.surname }}
            </option>
        </select>
    </td>
    
</tr>


<tr>
    <td></td>
    
</tr>

    </table>

    
    <button type="submit" @click="addFactory($event)" class="submit">Create</button>
 
     
  </form>
  <div class="map-container">
    <div  v-show="mapClicked !== 'NO_CLICK'" id="map"></div>
</div>
  <p v-show = "errorMsg == 'HasError'">Field inputs are invalid.</p>
</template>
<script setup>

import axios from 'axios';
import { ref, onMounted } from 'vue';
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

const route = useRoute();
const router = useRouter();
const factory = ref({ name: "",workingHours: {
    startHour: "08:00",
    endHour: "15:00"
  }, isWorking: false, location: {
    latitude: 0,
    longitude: 0,
    address: ""
  }, logoUrl: "", rate: 0});
const factoryValid = ref({ name: "a",workingHours: {
    startHour: "08:00",
    endHour: "15:00"
  }, isWorking: false,  location: {
    latitude: 0,
    longitude: 0,
    address: "a"
  }, logoUrl: "a", rate: -1.0});
const errorMsg = ref("NoError");

const managers = ref('');
const mainManager = ref(null);
const factoryReturn = ref('');
const managerFactory = ref({factoryId: -1, managerUsername: ''})

const serbiaExtent = transformExtent([18.817, 41.860, 23.006, 46.192], 'EPSG:4326', 'EPSG:3857');
const centerSerbia = transform([20.9115, 44.026], 'EPSG:4326', 'EPSG:3857');

const mapClicked = ref("NO_CLICK");
onMounted(() => {
    loadManagers();
})

function showMap()
{
    mapClicked.value = ref("CLICKED");
}
function loadManagers()
{
    axios.get('http://localhost:8080/WebShopAppREST/rest/getManagers').then(response => {
        if(response.data != "" && response.data.length > 0)
        {
            managers.value = response.data;
            mainManager.value = managers.value[0];
           
        }
        else {
            managers.value = '';
        }
    })
}


function addFactory(event)
{
    event.preventDefault();
    
    
    this.factoryValid = factory.value;
  
    
   if(!this.factoryValid.name || !this.factoryValid.logoUrl || !this.factoryValid.location.address
    || !this.factoryValid.workingHours.endHour || !this.factoryValid.workingHours.startHour)
{
   
    errorMsg.value = "HasError";
}
else if(this.managers.length == 0)
{
    axios.post("http://localhost:8080/WebShopAppREST/rest/factories/add", this.factory)
    .then(response => {
        console.log(response.data);
        this.factoryReturn = response.data;
        router.push({ path: '/register', query: { id: this.factoryReturn.id } });
        })
    .catch(error => {
        console.error('Error:', error);
    });
}
else
{
    
    axios.post("http://localhost:8080/WebShopAppREST/rest/factories/add", this.factory)
    .then(response => {
        console.log(response.data);
        this.factoryReturn = response.data; // Ensure this.factoryReturn is assigned properly

        // Update managerFactory with id and username
        this.managerFactory.factoryId = this.factoryReturn.id;
        this.managerFactory.managerUsername = this.mainManager.username;
        console.log(this.managerFactory);
        if(this.managers.length > 0)
        // Make the second axios post request within this then block to ensure it waits for the first one to complete
        return axios.post("http://localhost:8080/WebShopAppREST/rest/editManager", this.managerFactory);
    })
    .then(response => {
        console.log(response.data);
        router.push("/");
    })
    .catch(error => {
        console.error('Error:', error);
    });
    
    

    

    

}
    
   
}

onMounted(() => {
  
  const bufferedExtent = olExtentBuffer(serbiaExtent, 100000);

  const map = new Map({
    target: 'map', 
    layers: [
      new TileLayer({
        source: new OSM() 
      })
    ],
    view: new View({
      center: centerSerbia, 
      zoom: 8, 
      extent: bufferedExtent, 
      showFullExtent: true,
    })
  });
  const vectorSource = new VectorSource();
  const vectorLayer = new VectorLayer({
    source: vectorSource,
  });
  map.addLayer(vectorLayer);

  // Define a select interaction to handle click events
  const select = new Select({
    condition: click,
  });
  map.addInteraction(select);

  map.on('click', async (event) => {
    const coords = event.coordinate;
    const lonLat = toLonLat(coords, 'EPSG:3857');
   
 
    const address = await reverseGeocode(lonLat);
    console.log('Address:', address);

    factory.value.location.latitude = lonLat[1].toFixed(2);
    factory.value.location.longitude = lonLat[0].toFixed(2);
    factory.value.location.address = address;

    
    vectorSource.clear();
    vectorSource.addFeature(new Feature({
      geometry: new Point(coords),
    }));
  });

  async function reverseGeocode(coords) {
    const apiKey = '1e3b8927067945218adf7247a0f820e7';
   
  
    const url = `https://api.opencagedata.com/geocode/v1/json?q=${coords[1]},${coords[0]}&key=${apiKey}&language=sr-Latn`;
    try {
      const response = await fetch(url);
      const data = await response.json();
      console.log(data.results);
      if (data.results.length > 0) {
        return data.results[0].formatted;
      } else {
        return 'Address not found';
      }
    } catch (error) {
      console.error('Error fetching address:', error);
      return 'Error fetching address';
    }
  }

});
</script>

<style scoped>

*{
    padding: 0;
    margin: 0;
}

.map-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    
}
.fabrikaForma {
    margin: 0 auto;
    max-width: 520px;
    background: white;
    text-align: left;
    border-radius: 10px;
    padding: 25px;
}

h1 {
    margin: 0 auto;
    margin-top: 20px;
    color: #5a086a;
    padding: 0;
    background: white;
    max-width: 520px;
}

.fabrikaForma .label {
    color: gray;
    display: inline-block;
    margin: 20px 0 15px;
    font-size: 0.95em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;

}

.fabrikaForma input {
    padding: 2px 6px;
    box-sizing: border-box;
    border: none;
    border-bottom: 1px solid #aeaeae;
    color: #555;
    display: block;
    width: 350px;
}

.fabrikaForma .error{
    border-bottom: 2px solid red;
}

.fabrikaForma button {
    padding: 0.5rem;
    border: none;
    border-radius: 10px;
    background-color: #5a086a;
    color: white;
    cursor: pointer;
    width: 100%;
    height: 40px;
    font-size: 0.8em;
    text-transform: uppercase;
    letter-spacing: 1px;
    font-weight: bold;
    align-content: center;
    margin-top: 5px;
}

.fabrikaForma button:hover {
    background-color: #0056b3;
}

.fabrikaForma .reg_btn
{
    height: 30px;
    margin: 5px;
}

#map {
    width: 50%;
    height: 80vh;
  }

select {
    padding: 8px;
    border-radius: 5px;
    border: 1px solid #aeaeae;
    background-color: #fff;
    color: #555;
    width: 100%;
    box-sizing: border-box;
    font-size: 1em;
}

select:focus {
    border-color: #5a086a;
    outline: none;
}

option {
    padding: 10px;
    background-color: #fff;
    color: #555;
}

option:hover {
    background-color: #f0f0f0;
}
</style>