import cv2

img1 = cv2.imread(r'C:\Users\ismae\PycharmProjects\LectorQR\DePie.png')
img2 = cv2.imread(r'C:\Users\ismae\PycharmProjects\LectorQR\Sentado.png')

detector = cv2.QRCodeDetector()

data, bbox, stight_code = detector.detectAndDecode(img1)

if bbox is not None:
    print("Codigo correspondiente a: ",data)

    n_lines = len(bbox)
    for i in range(n_lines):
        point1 = tuple(bbox[i] [0])
        point2 = tuple(bbox[(i+1)% n_lines][0])
        cv2.line(img1, point1, point2, color=(255, 0, 0), thickness=2)

cv2.imshow("img1", img1)
print(len(bbox))
if data=="1":
    print("Estas de pie")