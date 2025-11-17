import { useCallback, useEffect, useMemo, useRef, useState } from "react";
import "./Map.css";
import MapPath from "./MapPath";
import MapBakeryList from "./MapBakeryList";
import MapView from "./MapView";
import { useKakaoLoader } from "../../hooks/useKakaoLoader";

const USER_ID = 1001;
const BAKERY_ENDPOINT = "http://localhost:3001/bakery";
const EARTH_RADIUS_METERS = 6371000;

const toRadians = (degree) => (degree * Math.PI) / 180;

const haversineDistance = (origin, target) => {
  if (!origin || !target) return Number.POSITIVE_INFINITY;

  const lat1 = origin.lat;
  const lon1 = origin.lng;
  const lat2 = target.lat;
  const lon2 = target.lng;

  if (
    !Number.isFinite(lat1) ||
    !Number.isFinite(lon1) ||
    !Number.isFinite(lat2) ||
    !Number.isFinite(lon2)
  ) {
    return Number.POSITIVE_INFINITY;
  }

  const dLat = toRadians(lat2 - lat1);
  const dLon = toRadians(lon2 - lon1);

  const a =
    Math.sin(dLat / 2) * Math.sin(dLat / 2) +
    Math.cos(toRadians(lat1)) *
      Math.cos(toRadians(lat2)) *
      Math.sin(dLon / 2) *
      Math.sin(dLon / 2);

  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

  return Math.round(EARTH_RADIUS_METERS * c);
};

const parseNumber = (value) => {
  const num = typeof value === "string" ? Number(value) : value;
  return Number.isFinite(num) ? num : null;
};

const Map = () => {
  const { kakao, status: kakaoStatus, error: kakaoError } = useKakaoLoader();
  const [bakeries, setBakeries] = useState([]);
  const [selectedBakeryId, setSelectedBakeryId] = useState(null);
  const [userLocation, setUserLocation] = useState(null);
  const [locationStatus, setLocationStatus] = useState("idle");
  const [locationErrorMessage, setLocationErrorMessage] = useState(null);
  const initialSelectionRef = useRef(false);

  useEffect(() => {
    const controller = new AbortController();

    const fetchBakeries = async () => {
      try {
        const response = await fetch(BAKERY_ENDPOINT, {
          signal: controller.signal,
        });
        if (!response.ok) throw new Error("Failed to fetch bakeries");
        const data = await response.json();
        const sanitized = Array.isArray(data) ? data : [];

        const normalized = sanitized.map((bakery) => ({
          ...bakery,
          lat: parseNumber(bakery.lat),
          lng: parseNumber(bakery.lng),
        }));

        setBakeries(normalized);

        if (!initialSelectionRef.current && normalized.length > 0) {
          setSelectedBakeryId(normalized[0].id);
          initialSelectionRef.current = true;
        }
      } catch (error) {
        if (error.name === "AbortError") return;
        console.error(error);
        setBakeries([]);
      }
    };

    fetchBakeries();

    return () => controller.abort();
  }, []);

  useEffect(() => {
    if (typeof navigator === "undefined" || !navigator.geolocation) {
      setLocationStatus("error");
      setLocationErrorMessage("브라우저에서 위치 정보 사용이 제한되어 있어요.");
      return;
    }

    setLocationStatus("loading");
    setLocationErrorMessage(null);

    const handleSuccess = (position) => {
      const { latitude, longitude } = position.coords;
      setUserLocation({
        lat: latitude,
        lng: longitude,
      });
      setLocationStatus("ready");
    };

    const handleError = (error) => {
      console.warn("Geolocation error", error);
      switch (error.code) {
        case error.PERMISSION_DENIED:
          setLocationErrorMessage(
            "위치 권한이 필요해요. 브라우저 설정을 확인해주세요."
          );
          break;
        case error.POSITION_UNAVAILABLE:
          setLocationErrorMessage(
            "현재 위치를 확인할 수 없어요. 네트워크 상태를 확인해주세요."
          );
          break;
        case error.TIMEOUT:
          setLocationErrorMessage(
            "현재 위치 정보를 가져오는데 시간이 너무 오래 걸려요."
          );
          break;
        default:
          setLocationErrorMessage("위치 정보를 가져오는 중 오류가 발생했어요.");
      }
      setLocationStatus("error");
    };

    const watchId = navigator.geolocation.watchPosition(
      handleSuccess,
      handleError,
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0,
      }
    );

    return () => {
      navigator.geolocation.clearWatch(watchId);
    };
  }, []);

  const bakeriesWithDistance = useMemo(() => {
    if (bakeries.length === 0) return [];

    return bakeries.map((bakery) => {
      const fallbackDistance = Number.isFinite(bakery.distanceMeters)
        ? Number(bakery.distanceMeters)
        : Number.POSITIVE_INFINITY;

      if (!userLocation) {
        return {
          ...bakery,
          distanceMeters: fallbackDistance,
        };
      }

      const distance = haversineDistance(userLocation, {
        lat: bakery.lat,
        lng: bakery.lng,
      });

      return {
        ...bakery,
        distanceMeters:
          Number.isFinite(distance) && distance !== 0
            ? distance
            : fallbackDistance,
      };
    });
  }, [bakeries, userLocation]);

  const handleSelectBakery = useCallback((bakeryId) => {
    setSelectedBakeryId(bakeryId);
  }, []);

  const mapErrorMessage = useMemo(() => {
    if (!kakaoError) return null;
    switch (kakaoError.message) {
      case "KAKAO_SDK_MISSING_APP_KEY":
        return "카카오 지도 API 키가 설정되지 않았어요. .env 파일에 VITE_KAKAO_MAP_KEY를 추가해주세요.";
      case "KAKAO_SDK_LOAD_FAILED":
        return "카카오 지도를 불러오지 못했어요. 네트워크 상태를 확인해주세요.";
      default:
        return kakaoError.message;
    }
  }, [kakaoError]);

  return (
    <div className="Map">
      {/* 순례길 추천 영역 */}
      <div className="MapSidebar" aria-label="순례길 추천">
        <MapPath />
      </div>

      {/* 빵집 리스트 영역 */}
      <aside className="MapListPane" aria-label="빵집 목록">
        <MapBakeryList
          bakerys={bakeriesWithDistance}
          selectedBakeryId={selectedBakeryId}
          onSelectBakery={handleSelectBakery}
        />
      </aside>

      {/* 지도 영역 */}
      <section className="MapMainPane" aria-label="지도 영역">
        <MapView
          kakao={kakao}
          bakeries={bakeriesWithDistance}
          selectedBakeryId={selectedBakeryId}
          onSelectBakery={handleSelectBakery}
          userLocation={userLocation}
          locationStatus={locationStatus}
          locationErrorMessage={locationErrorMessage}
          mapStatus={kakaoStatus}
          mapErrorMessage={mapErrorMessage}
        />
      </section>
    </div>
  );
};

export default Map;
