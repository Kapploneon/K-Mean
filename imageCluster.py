import numpy as np
import cv2
import sys
import os

img = cv2.imread(sys.argv[1])
#img = cv2.imread("image1.jpg")
Z = img.reshape((-1,3))
# convert to np.float32
Z = np.float32(Z)
# define criteria, number of clusters(K) and apply kmeans()
criteria = (cv2.TERM_CRITERIA_EPS + cv2.TERM_CRITERIA_MAX_ITER, 10, 1.0)
K = 8
ret,label,center=cv2.kmeans(Z,K,None,criteria,10,cv2.KMEANS_RANDOM_CENTERS)
# Now convert back into uint8, and make original image
center = np.uint8(center)
res = center[label.flatten()]
res2 = res.reshape((img.shape))

if not os.path.exists('clusterImages'):
    os.makedirs('clusterImages')
os.chdir('clusterImages')
cv2.imwrite(sys.argv[1].split('.')[0] + '_out.jpg',res2)