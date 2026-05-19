from flask import Flask, jsonify
import cv2
import time
import requests
import numpy as np

app = Flask(__name__)

# =========================
# CONFIG
# =========================
CAMERA_DEVICE = "/dev/video8"
API_TOKEN = "72ea2ff3d3b781f368a7204a75a1108b26ecbf23"
FRAME_COUNT = 6


# =========================
# CAMERA QUALITY FUNCTION
# =========================
def sharpness_score(frame):
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)
    return cv2.Laplacian(gray, cv2.CV_64F).var()


# =========================
# STABLE CAMERA CAPTURE
# =========================
def capture_best_frame(path="capture.jpg"):
    cap = cv2.VideoCapture(CAMERA_DEVICE, cv2.CAP_V4L2)

    if not cap.isOpened():
        raise RuntimeError("Camera not found or in use")

    # Force stable settings (VERY IMPORTANT on Pi webcams)
    cap.set(cv2.CAP_PROP_FRAME_WIDTH, 640)
    cap.set(cv2.CAP_PROP_FRAME_HEIGHT, 480)
    cap.set(cv2.CAP_PROP_FOURCC, cv2.VideoWriter_fourcc(*'MJPG'))

    # =========================
    # WARM-UP (fixes blur issue)
    # =========================
    for _ in range(12):
        cap.read()
        time.sleep(0.1)

    best_frame = None
    best_score = -1

    # =========================
    # FRAME SAMPLING
    # =========================
    for i in range(FRAME_COUNT):
        ret, frame = cap.read()
        if not ret:
            continue

        score = sharpness_score(frame)
        print(f"Frame {i} sharpness: {score:.2f}")

        if score > best_score:
            best_score = score
            best_frame = frame

        time.sleep(0.15)

    cap.release()

    if best_frame is None:
        raise RuntimeError("Failed to capture any valid frame")

    # Resize + save
    best_frame = cv2.resize(best_frame, (640, 480))
    cv2.imwrite(path, best_frame, [int(cv2.IMWRITE_JPEG_QUALITY), 90])

    print(f"📷 Selected best frame sharpness: {best_score:.2f}")
    return path


# =========================
# LOGMEAL API CALL
# =========================
def recognize_food(image_path):
    url = "https://api.logmeal.com/v2/image/recognition/complete"

    with open(image_path, "rb") as f:
        files = {"image": f}
        headers = {"Authorization": f"Bearer {API_TOKEN}"}

        r = requests.post(url, files=files, headers=headers)

    print("STATUS:", r.status_code)
    print("RAW:", r.text[:300])

    if r.status_code != 200:
        return None

    data = r.json()

    # Handle "non food"
    if "foodType" in data:
        if data["foodType"][0]["name"].lower() == "non food":
            return None

    results = data.get("recognition_results", [])

    if not results:
        return None

    best = results[0]

    name = best.get("name")
    confidence = best.get("prob", 0)

    print(f"🔍 Detected: {name} ({confidence:.2f})")

    if confidence < 0.60:
        print("⚠️ Low confidence result rejected")
        return None

    return name.lower()


# =========================
# MAIN SCAN PIPELINE
# =========================
def scan():
    print("📷 Capturing...")
    img = capture_best_frame()

    print("🔍 Recognizing...")
    result = recognize_food(img)

    return result


# =========================
# API ENDPOINT
# =========================
@app.route("/scan", methods=["GET"])
def scan_route():
    try:
        result = scan()

        if result:
            return jsonify({"result": result})

        return jsonify({"error": "no confident result"}), 500

    except Exception as e:
        print("🔥 ERROR:", e)
        return jsonify({"error": str(e)}), 500


# =========================
# START SERVER
# =========================
if __name__ == "__main__":
    print("🚀 Scanner running on port 5000")
    app.run(host="0.0.0.0", port=5000)