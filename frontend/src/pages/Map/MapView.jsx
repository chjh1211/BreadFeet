import { useEffect, useMemo, useRef } from "react";
import "./Map.css";

const DEFAULT_CENTER = {
  lat: 35.888,
  lng: 128.6106,
};

const COOKIE_PATH =
  "M510.52 255.82c-69.97-.85-126.47-57.69-126.47-127.86-70.17 0-127-56.49-127.86-126.45-27.26-4.14-55.13.3-79.72 12.82l-69.13 35.22a132.221 132.221 0 0 0-57.79 57.81l-35.1 68.88a132.645 132.645 0 0 0-12.82 80.95l12.08 76.27a132.521 132.521 0 0 0 37.16 72.96l54.77 54.76a132.036 132.036 0 0 0 72.71 37.06l76.71 12.15c27.51 4.36 55.7-.11 80.53-12.76l69.13-35.21a132.273 132.273 0 0 0 57.79-57.81l35.1-68.88c12.56-24.64 17.01-52.58 12.91-79.91zM176 368c-17.67 0-32-14.33-32-32s14.33-32 32-32 32 14.33 32 32-14.33 32-32 32zm32-160c-17.67 0-32-14.33-32-32s14.33-32 32-32 32 14.33 32 32-14.33 32-32 32zm160 128c-17.67 0-32-14.33-32-32s14.33-32 32-32 32 14.33 32 32-14.33 32-32 32z";

const createCookieDataUrl = (fill = "#c17f45", shadow = "#6b4523") => {
  const svg = `<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512">
      <defs>
        <filter id="cookie-shadow" x="-20%" y="-20%" width="140%" height="140%">
          <feDropShadow dx="0" dy="6" stdDeviation="6" flood-color="${shadow}" flood-opacity="0.35"/>
        </filter>
      </defs>
      <path d="${COOKIE_PATH}" fill="${fill}" filter="url(#cookie-shadow)"/>
    </svg>`;
  return `data:image/svg+xml;charset=utf-8,${encodeURIComponent(svg)}`;
};

const USER_MARKER_SVG = `<svg width="56" height="64" viewBox="0 0 56 64" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M28 0C13.0883 0 1 12.0883 1 27C1 47.25 28 64 28 64C28 64 55 47.25 55 27C55 12.0883 42.9117 0 28 0Z" fill="#3575FF"/><circle cx="28" cy="27" r="10.5" fill="white"/></svg>`;
const USER_MARKER_SRC = `data:image/svg+xml;charset=utf-8,${encodeURIComponent(
  USER_MARKER_SVG
)}`;

const formatLatLng = (lat, lng, kakao) => {
  if (!kakao) return null;
  if (typeof lat !== "number" || typeof lng !== "number") return null;
  return new kakao.maps.LatLng(lat, lng);
};

const createMarkerImage = (kakao, { src, size = [34, 38], offset = [17, 38] }) => {
  if (!kakao) return null;
  return new kakao.maps.MarkerImage(
    src,
    new kakao.maps.Size(...size),
    {
      offset: new kakao.maps.Point(...offset),
    }
  );
};

const BOUNDS_PADDING = { top: 44, right: 44, bottom: 44, left: 44 };

const MapView = ({
  kakao,
  bakeries,
  selectedBakeryId,
  onSelectBakery,
  userLocation,
  locationStatus = "idle",
  locationErrorMessage,
  mapStatus = "idle",
  mapErrorMessage,
  aiPathBakeries = []
}) => {
  const containerRef = useRef(null);
  const mapRef = useRef(null);
  const markersRef = useRef(new Map());
  const userMarkerRef = useRef(null);
  const userCircleRef = useRef(null);
  const polylineRef = useRef(null); // 기존: 거리 측정용
  const distanceOverlayRef = useRef(null);
  const hasAutoCenteredRef = useRef(false);
  
  const aiPathPolylineRef = useRef(null);
  // 🟢 AI 경로 순번 오버레이 배열 Ref
  const aiPathOverlaysRef = useRef([]); 

  const markerImages = useMemo(() => {
    if (!kakao) return null;
    return {
      defaultImage: createMarkerImage(kakao, {
        src: createCookieDataUrl("#c17f45", "#8b521d"),
        size: [46, 52],
        offset: [23, 52],
      }),
      selectedImage: createMarkerImage(kakao, {
        src: createCookieDataUrl("#f0a35e", "#b6671f"),
        size: [56, 62],
        offset: [28, 62],
      }),
      userImage: createMarkerImage(kakao, {
        src: USER_MARKER_SRC,
        size: [46, 52],
        offset: [23, 52],
      }),
    };
  }, [kakao]);

  useEffect(() => {
    if (
      !kakao ||
      mapStatus !== "ready" ||
      !containerRef.current ||
      mapRef.current
    ) {
      return;
    }

    const centerLatLng =
      formatLatLng(userLocation?.lat, userLocation?.lng, kakao) ??
      new kakao.maps.LatLng(DEFAULT_CENTER.lat, DEFAULT_CENTER.lng);

    mapRef.current = new kakao.maps.Map(containerRef.current, {
      center: centerLatLng,
      level: 4,
    });
  }, [kakao, userLocation, mapStatus]);

  useEffect(() => {
    if (!kakao || !mapRef.current) return;

    // Cleanup existing markers
    markersRef.current.forEach((marker) => marker.setMap(null));
    markersRef.current.clear();

    const bounds = new kakao.maps.LatLngBounds();
    let hasBounds = false;

    bakeries.forEach((bakery) => {
      const position = formatLatLng(bakery.lat, bakery.lng, kakao);
      if (!position) return;

      const marker = new kakao.maps.Marker({
        map: mapRef.current,
        position,
        clickable: true,
        image: markerImages?.defaultImage ?? null,
        zIndex: bakery.id === selectedBakeryId ? 5 : 1, 
      });

      kakao.maps.event.addListener(marker, "click", () => {
        onSelectBakery?.(bakery.id);
      });

      markersRef.current.set(bakery.id, marker);

      bounds.extend(position);
      hasBounds = true;
    });

    const userPosition = formatLatLng(userLocation?.lat, userLocation?.lng, kakao);
    if (userPosition) {
      bounds.extend(userPosition);
      hasBounds = true;
    }

    if (hasBounds) {
      mapRef.current.setBounds(
        bounds,
        BOUNDS_PADDING.top,
        BOUNDS_PADDING.right,
        BOUNDS_PADDING.bottom,
        BOUNDS_PADDING.left
      );
    }
  }, [bakeries, kakao, onSelectBakery, selectedBakeryId, userLocation, markerImages]);

  // ... (선택 마커 이미지 변경, 선택 마커로 지도 이동, 사용자 위치 마커/원 표시 로직은 변경 없이 유지)

  useEffect(() => {
    if (!mapRef.current || !markerImages) return;

    markersRef.current.forEach((marker, id) => {
      marker.setImage(
        id === selectedBakeryId
          ? markerImages.selectedImage ?? null
          : markerImages.defaultImage ?? null
      );
      marker.setZIndex(id === selectedBakeryId ? 7 : 1);
    });
  }, [markerImages, selectedBakeryId]);

  useEffect(() => {
    if (!mapRef.current || !selectedBakeryId) return;
    const marker = markersRef.current.get(selectedBakeryId);
    if (!marker) return;
    const position = marker.getPosition();
    if (!position) return;
    mapRef.current.panTo(position);
  }, [selectedBakeryId]);

  useEffect(() => {
    if (!kakao || !mapRef.current || !markerImages) return;

    const position = formatLatLng(userLocation?.lat, userLocation?.lng, kakao);
    
    if (!position) {
      if (userMarkerRef.current) {
        userMarkerRef.current.setMap(null);
        userMarkerRef.current = null;
      }
      if (userCircleRef.current) {
        userCircleRef.current.setMap(null);
        userCircleRef.current = null;
      }
      hasAutoCenteredRef.current = false;
      return;
    }

    if (!userMarkerRef.current) {
      userMarkerRef.current = new kakao.maps.Marker({
        map: mapRef.current,
        position,
        image: markerImages.userImage ?? null,
        zIndex: 10,
      });
    } else {
      userMarkerRef.current.setMap(mapRef.current);
      userMarkerRef.current.setImage(markerImages.userImage ?? null);
      userMarkerRef.current.setPosition(position);
    }

    if (userCircleRef.current) {
      userCircleRef.current.setMap(null);
      userCircleRef.current = null;
    }

    userCircleRef.current = new kakao.maps.Circle({
      center: position,
      radius: 500,
      strokeWeight: 2,
      strokeColor: "#3575ff",
      strokeOpacity: 0.7,
      strokeStyle: "solid",
      fillColor: "#3575ff",
      fillOpacity: 0.15,
    });
    userCircleRef.current.setMap(mapRef.current);

    if (!hasAutoCenteredRef.current) {
      mapRef.current.panTo(position);
      hasAutoCenteredRef.current = true;
    }
  }, [kakao, markerImages, userLocation]);

  // 🟢 AI 경로 (Polyline 및 순번 오버레이) 표시 로직
  useEffect(() => {
    if (!kakao || !mapRef.current) return;

    const clearAiPathElements = () => {
        if (aiPathPolylineRef.current) {
            aiPathPolylineRef.current.setMap(null);
        }
        aiPathOverlaysRef.current.forEach(overlay => overlay.setMap(null));
        aiPathOverlaysRef.current = [];
    };

    // AI 경로가 없으면 기존 Polyline과 오버레이를 제거
    if (aiPathBakeries.length < 2) {
      clearAiPathElements();
      return;
    }

    // 경로를 구성할 LatLng 배열 (AI 경로 빵집들의 좌표)
    const path = aiPathBakeries.map(bakery => 
      formatLatLng(bakery.lat, bakery.lng, kakao)
    ).filter(latlng => latlng !== null); 

    if (path.length < 2) {
        clearAiPathElements();
        return;
    }
    
    // Polyline 객체 생성 또는 업데이트
    if (!aiPathPolylineRef.current) {
      aiPathPolylineRef.current = new kakao.maps.Polyline({
        path: path,
        strokeWeight: 4, 
        strokeColor: '#62321a', // 빵 컨셉에 맞게 짙은 갈색 계열로 변경
        strokeOpacity: 0.8, 
        strokeStyle: 'solid', 
        zIndex: 2, 
      });
      aiPathPolylineRef.current.setMap(mapRef.current);
    } else {
      aiPathPolylineRef.current.setPath(path);
      aiPathPolylineRef.current.setMap(mapRef.current);
    }
    
    // 경로에 순번 오버레이 추가 및 지도 이동
    clearAiPathElements(); // 기존 오버레이 삭제 후 새로 생성
    aiPathPolylineRef.current.setMap(mapRef.current);
    const bounds = new kakao.maps.LatLngBounds();

    path.forEach((latlng, index) => {
        bounds.extend(latlng);
        
        // 🟢 순번 오버레이 생성
        const content = document.createElement('div');
        content.className = 'AIPathNumberOverlay';
        content.textContent = index + 1;
        // 💡 CSS를 통해 이 div를 동그란 뱃지 형태로 꾸며야 합니다. (Map.css에 추가 필요)
        
        const overlay = new kakao.maps.CustomOverlay({
            content: content,
            position: latlng,
            xAnchor: 0.5,
            yAnchor: 1.2, // 마커 이미지 위쪽에 위치하도록 조정
            zIndex: 3 // Polyline보다 위, 선택 마커보다 아래
        });
        
        overlay.setMap(mapRef.current);
        aiPathOverlaysRef.current.push(overlay);
    });

    mapRef.current.setBounds(
        bounds,
        BOUNDS_PADDING.top,
        BOUNDS_PADDING.right,
        BOUNDS_PADDING.bottom,
        BOUNDS_PADDING.left
    );
    
    // AI 경로가 활성화되면 거리 측정 경로를 숨깁니다.
    if (polylineRef.current) {
      polylineRef.current.setMap(null);
    }
    if (distanceOverlayRef.current) {
      distanceOverlayRef.current.setMap(null);
    }

  }, [kakao, aiPathBakeries]); // aiPathBakeries가 변경될 때마다 실행


  useEffect(() => {
    return () => {
      markersRef.current.forEach((marker) => marker.setMap(null));
      markersRef.current.clear();
      if (userMarkerRef.current) {
        userMarkerRef.current.setMap(null);
        userMarkerRef.current = null;
      }
      if (userCircleRef.current) {
        userCircleRef.current.setMap(null);
        userCircleRef.current = null;
      }
      if (polylineRef.current) {
        polylineRef.current.setMap(null);
        polylineRef.current = null;
      }
      if (aiPathPolylineRef.current) {
        aiPathPolylineRef.current.setMap(null);
        aiPathPolylineRef.current = null;
      }
      // 🟢 AI 경로 순번 오버레이 cleanup
      aiPathOverlaysRef.current.forEach(overlay => overlay.setMap(null));
      aiPathOverlaysRef.current = [];
      
      if (distanceOverlayRef.current) {
        distanceOverlayRef.current.setMap(null);
        distanceOverlayRef.current = null;
      }
    };
  }, []);

  const overlayMessage = useMemo(() => {
    if (mapStatus === "loading" || mapStatus === "idle") {
      return "지도를 불러오는 중이에요...";
    }
    if (mapStatus === "error") {
      return mapErrorMessage ?? "지도를 불러오지 못했어요.";
    }
    if (locationStatus === "loading") {
      return "현재 위치를 확인하는 중이에요...";
    }
    if (locationStatus === "error") {
      return (
        locationErrorMessage ??
        "위치 정보를 가져올 수 없어요. 브라우저 설정을 확인해주세요."
      );
    }
    if (mapStatus === "ready" && bakeries.length === 0) {
      return "표시할 빵집 정보를 찾을 수 없어요.";
    }
    return null;
  }, [
    bakeries.length,
    locationErrorMessage,
    locationStatus,
    mapErrorMessage,
    mapStatus,
  ]);

  // ⚠️ 기존 거리 측정 useEffect 수정 (AI 경로 활성화 시 실행하지 않도록 보호)
  useEffect(() => {
    if (!kakao || !mapRef.current) return;
    
    const isAiPathActive = aiPathBakeries.length >= 2;

    const clearDistanceOverlay = () => {
      if (polylineRef.current) {
        polylineRef.current.setMap(null);
      }
      if (distanceOverlayRef.current) {
        distanceOverlayRef.current.setMap(null);
      }
    };
    
    // 🟢 AI 경로가 활성화되면 거리 측정 경로를 표시하지 않음
    if (isAiPathActive) {
        clearDistanceOverlay();
        return;
    }

    const selectedBakery = bakeries.find(
      (bakery) => bakery.id === selectedBakeryId
    );

    if (!selectedBakery || !userLocation) {
      clearDistanceOverlay();
      return;
    }

    const bakeryMarker = markersRef.current.get(selectedBakeryId);
    const userPosition = formatLatLng(
      userLocation.lat,
      userLocation.lng,
      kakao
    );
    const bakeryPosition = bakeryMarker?.getPosition();

    if (!bakeryMarker || !userPosition || !bakeryPosition) {
      clearDistanceOverlay();
      return;
    }

   if (!polylineRef.current) {
      polylineRef.current = new kakao.maps.Polyline({
        strokeWeight: 5, 
        strokeColor: "#62321aff", 
        strokeOpacity: 0.5, 
        strokeStyle: "dash", 
      });
    }

    polylineRef.current.setPath([userPosition, bakeryPosition]);
    polylineRef.current.setMap(mapRef.current);

    const distanceMeters = selectedBakery.distanceMeters;
    let distanceText = "-";
    if (Number.isFinite(distanceMeters)) {
      distanceText =
        distanceMeters < 1000
          ? `${Math.round(distanceMeters)}m`
          : `${(distanceMeters / 1000).toFixed(1)}km`;
    }

    const content = document.createElement("div");
    content.className = "MapDistanceInfo";
    content.textContent = `빵집까지 ${distanceText}!`;

    if (!distanceOverlayRef.current) {
      distanceOverlayRef.current = new kakao.maps.CustomOverlay({
        yAnchor: 1.4,
        zIndex: 8,
      });
    }

    distanceOverlayRef.current.setContent(content);
    distanceOverlayRef.current.setPosition(bakeryPosition);
    distanceOverlayRef.current.setMap(mapRef.current);
  }, [bakeries, kakao, selectedBakeryId, userLocation, aiPathBakeries]); 

  return (
    <div className="MapCanvasWrapper">
      <div className="MapCanvas" ref={containerRef} role="presentation" />
      {overlayMessage && (
        <div className="MapCanvasOverlay" role="status">
          {overlayMessage}
        </div>
      )}
    </div>
  );
};

export default MapView;