import os
from fastapi import FastAPI

app = FastAPI()

info = os.getenv("INFO", "1234")

@app.get("/")
def read_root():
    return { "message": info }