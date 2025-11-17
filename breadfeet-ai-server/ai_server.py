import pandas as pd
import numpy as np
from fastapi import FastAPI
from pydantic import BaseModel
import joblib

app = FastAPI()

model = joblib.load("bread_model.pkl")

BREAD_TYPES = ['식빵', '크루아상', '바게트', '크림빵', '단팥빵', '소보로빵', '소시지빵', '베이글', '도넛']


class SurveyInput(BaseModel):
    gender: str
    age: str
    butter_flavor: int
    pastry_pref: int
    sweetness_pref: int
    savory_pref: int
    crispiness_pref: int
    soft_moist_pref: int
    chewy_pref: int
    health_pref: int
    filled_pref: int
    freshness_importance: int


@app.post("/predict")
def predict(survey: SurveyInput):
    data = {
        "gender": [survey.gender],
        "age": [survey.age],
        "butter_flavor": [survey.butter_flavor],
        "pastry_pref": [survey.pastry_pref],
        "sweetness_pref": [survey.sweetness_pref],
        "savory_pref": [survey.savory_pref],
        "crispiness_pref": [survey.crispiness_pref],
        "soft_moist_pref": [survey.soft_moist_pref],
        "chewy_pref": [survey.chewy_pref],
        "health_pref": [survey.health_pref],
        "filled_pref": [survey.filled_pref],
        "freshness_importance": [survey.freshness_importance],
    }

    X_input = pd.DataFrame(data)

    proba = model.predict_proba(X_input)[0]  # (9,) 벡터

    probs = [
        {"bread": bread, "prob": float(p)}
        for bread, p in zip(BREAD_TYPES, proba)
    ]

    top_idx = int(np.argmax(proba))
    return {
        "top1_bread": BREAD_TYPES[top_idx],
        "top1_prob": float(proba[top_idx]),
        "probs": probs
    }
