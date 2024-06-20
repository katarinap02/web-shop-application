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

onMounted(() => {
  // Initialize the map
  const map = new Map({
    target: 'map', // The id of the div where the map will be rendered
    layers: [
      new TileLayer({
        source: new OSM() // OpenStreetMap layer
      })
    ],
    view: new View({
      center: [0, 0], // Coordinates in EPSG:3857 projection (long, lat)
      zoom: 2 // Initial zoom level
    })
  });
});
</script>

<style scoped>
#map {
  position: absolute;
  margin: 0;
  padding: 0;
  height: 500px;
  width: 99%;
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