<template>
    <div id="map"></div>
</template>

<script setup>

import "ol/ol.css";
// This is library of openlayer for handle map
import Map from "ol/Map";
import View from "ol/View";
import { defaults as defaultControls, ScaleLine } from "ol/control";
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer';
import {OSM, Vector as VectorSource} from 'ol/source';
import { onMounted } from 'vue';
import { transformExtent, transform } from 'ol/proj';
import { buffer as olExtentBuffer } from 'ol/extent';
import { GeoJSON } from 'ol/format';
import Select from 'ol/interaction/Select';
import { click } from 'ol/events/condition';
import { fromLonLat, toLonLat } from 'ol/proj';
import Point from 'ol/geom/Point';
import Feature from 'ol/Feature';

const serbiaExtent = transformExtent([18.817, 41.860, 23.006, 46.192], 'EPSG:4326', 'EPSG:3857');
const centerSerbia = transform([20.9115, 44.026], 'EPSG:4326', 'EPSG:3857');

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
#map {
  width: 50%;
  height: 100vh;
}
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
#nav {
  padding: 30px;
}
#nav a {
  font-weight: bold;
  color: #2c3e50;
}
#nav a.router-link-exact-active {
  color: #42b983;
}
</style>