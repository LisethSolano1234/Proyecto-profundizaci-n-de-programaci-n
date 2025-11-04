let map, directionsService, directionsRenderer;
let stops = [];
let markers = [];

function initMap() {
  map = new google.maps.Map(document.getElementById("map"), {
    center: { lat: 4.437165583673859, lng: -75.15256574557657 }, // Ibagué
    zoom: 12,
    mapTypeControl: false, ,
    streetViewControl: false,
  });

  directionsService = new google.maps.DirectionsService();
  directionsRenderer = new google.maps.DirectionsRenderer({ map });

  const input = document.getElementById("searchBox");
  const autocomplete = new google.maps.places.Autocomplete(input);
  autocomplete.bindTo("bounds", map);

  // Agregar punto
  document.getElementById("addStop").addEventListener("click", () => {
    const place = autocomplete.getPlace();
    if (!place || !place.geometry) {
      alert("Por favor selecciona un lugar válido.");
      return;
    }

    const location = place.geometry.location;
    stops.push(location);

    const marker = new google.maps.Marker({
      position: location,
      map,
      label: `${stops.length}`,
      animation: google.maps.Animation.DROP,
    });

    markers.push(marker);
    map.panTo(location);
  });

  document.getElementById("drawRoute").addEventListener("click", drawRoute);
  document.getElementById("clearRoute").addEventListener("click", clearRoute);
  document.getElementById("saveRoute").addEventListener("click", saveRoute);
}

function drawRoute() {
  if (stops.length < 2) {
    alert("Agrega al menos dos puntos.");
    return;
  }

  const waypoints = stops.slice(1, -1).map((loc) => ({ location: loc }));
  directionsService
    .route({
      origin: stops[0],
      destination: stops[stops.length - 1],
      waypoints,
      travelMode: google.maps.TravelMode.DRIVING,
    })
    .then((result) => {
      directionsRenderer.setDirections(result);
    })
    .catch(() => alert("No se pudo trazar la ruta."));
}

function clearRoute() {
  stops = [];
  markers.forEach((m) => m.setMap(null));
  markers = [];
  directionsRenderer.setDirections({ routes: [] });
}

function saveRoute() {
  if (stops.length === 0) {
    alert("Primero crea una ruta.");
    return;
  }

  const trayectos = stops.map((loc, i) => ({
    latitud: loc.lat(),
    longitud: loc.lng(),
    ordenParada: i + 1,
    loginRegistro: "admin",
  }));

  console.log("Trayectos a guardar:", trayectos);
  alert("Simulación: trayectos listos para guardar en base de datos.");
  document.getElementById("btnHistorial").addEventListener("click", async () => {
    try {
      const response = await fetch("http://localhost:8080/trayectos/vehiculo/1"); // <-- cambia 1 por el ID del vehículo actual
      if (!response.ok) throw new Error("Error al obtener historial de rutas");

      const trayectos = await response.json();

      if (trayectos.length === 0) {
        alert("No hay rutas registradas para este vehículo.");
        return;
      }

      // Mostrar rutas guardadas en el mapa
      trayectos.forEach(t => {
        new google.maps.Marker({
          position: { lat: t.latitud, lng: t.longitud },
          map,
          title: `Ubicación: ${t.ubicacion}`
        });
      });

      alert(`Se han cargado ${trayectos.length} puntos del historial.`);
    } catch (err) {
      console.error(err);
      alert("Error al cargar historial de rutas.");
    }
    let historialPolyline; // Para dibujar la línea del recorrido

    document.getElementById("btnHistorial").addEventListener("click", async () => {
      try {
        const idVehiculo = 1; // <-- Cambia esto según el vehículo que quieras ver
        const response = await fetch(`http://localhost:8080/trayectos/vehiculo/${idVehiculo}`);

        if (!response.ok) throw new Error("Error al obtener historial de rutas");

        const trayectos = await response.json();

        if (trayectos.length === 0) {
          alert("No hay rutas registradas para este vehículo.");
          return;
        }

        // Limpiar marcador o ruta previa si existe
        if (historialPolyline) historialPolyline.setMap(null);

        const puntos = trayectos.map(t => ({
          lat: t.latitud,
          lng: t.longitud
        }));

        // Dibujar los puntos del trayecto
        puntos.forEach((p, index) => {
          new google.maps.Marker({
            position: p,
            map,
            title: `Parada ${index + 1}`
          });
        });

        // Dibujar la línea del recorrido
        historialPolyline = new google.maps.Polyline({
          path: puntos,
          geodesic: true,
          strokeColor: "#1e90ff",
          strokeOpacity: 0.8,
          strokeWeight: 4,
          map: map
        });

        // Centrar el mapa en el recorrido
        const bounds = new google.maps.LatLngBounds();
        puntos.forEach(p => bounds.extend(p));
        map.fitBounds(bounds);

        alert(`Se han cargado ${trayectos.length} puntos del historial.`);
      } catch (err) {
        console.error(err);
        alert("Error al cargar historial de rutas.");
      }
    });

  });

}
