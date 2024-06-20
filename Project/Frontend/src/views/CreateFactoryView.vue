<script setup>
import HelloWorld from '@/components/HelloWorld.vue';
import CreateFactory from '@/components/CreateFactory.vue';
import Navbar from '@/components/basic-components/Navbar.vue';
import Map from '@/components/Map.vue';
</script>

<template>
<main>
    <div>
        <Navbar/>
    </div>
    <div>
    <CreateFactory/>
    </div>
    <div>
        <Map/>
    </div>
</main>
    
</template>