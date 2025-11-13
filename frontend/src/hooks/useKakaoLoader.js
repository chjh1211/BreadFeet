import { useEffect, useState } from "react";

let kakaoLoaderPromise = null;

const loadKakaoSDK = (appKey) => {
  if (typeof window === "undefined") {
    return Promise.reject(new Error("KAKAO_SDK_NO_WINDOW"));
  }

  if (!appKey) {
    return Promise.reject(new Error("KAKAO_SDK_MISSING_APP_KEY"));
  }

  if (window.kakao && window.kakao.maps) {
    return Promise.resolve(window.kakao);
  }

  if (!kakaoLoaderPromise) {
    kakaoLoaderPromise = new Promise((resolve, reject) => {
      const existing = document.querySelector("script[data-kakao-maps-sdk]");
      if (existing) {
        existing.addEventListener("load", () => {
          if (window.kakao && window.kakao.maps) {
            resolve(window.kakao);
          } else {
            reject(new Error("KAKAO_SDK_LOAD_EMPTY"));
          }
        });
        existing.addEventListener("error", () =>
          reject(new Error("KAKAO_SDK_LOAD_FAILED"))
        );
        return;
      }

      const script = document.createElement("script");
      script.src = `https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${appKey}&libraries=services`;
      script.async = true;
      script.defer = true;
      script.dataset.kakaoMapsSdk = "true";

      script.onload = () => {
        if (!window.kakao || !window.kakao.maps) {
          reject(new Error("KAKAO_SDK_LOAD_EMPTY"));
          return;
        }

        window.kakao.maps.load(() => {
          resolve(window.kakao);
        });
      };

      script.onerror = () => {
        reject(new Error("KAKAO_SDK_LOAD_FAILED"));
      };

      document.head.appendChild(script);
    });
  }

  return kakaoLoaderPromise;
};

export const useKakaoLoader = () => {
  const [state, setState] = useState({
    status: "idle",
    error: null,
  });

  useEffect(() => {
    let cancelled = false;
    const appKey = import.meta.env.VITE_KAKAO_MAP_KEY;

    setState({ status: "loading", error: null });

    loadKakaoSDK(appKey)
      .then((kakao) => {
        if (!cancelled) {
          setState({ status: "ready", error: null, kakao });
        }
      })
      .catch((error) => {
        if (!cancelled) {
          setState({ status: "error", error });
        }
      });

    return () => {
      cancelled = true;
    };
  }, []);

  return {
    kakao: state.status === "ready" ? window.kakao : null,
    status: state.status,
    error: state.error,
  };
};


