# Converts the images in this folder to 16x16 PNGs using white as the transparency color

OPTIONS=--export-png-color-mode=GrayAlpha_16

ZOOMS="100 125 150 200 250 300"
for zoom in ${ZOOMS}; do
    if [ ! -e zoom_${zoom} ]; then
        mkdir -v "zoom_${zoom}"
    fi
done


for file in *.svg; do
    echo "Converting ${file}..."
    base=`basename ${file} .svg`
    inkscape ${base}.svg -o zoom_100/${base}.png ${OPTIONS} -w 16 -h 16
    inkscape ${base}.svg -o zoom_125/${base}.png ${OPTIONS} -w 20 -h 20
    inkscape ${base}.svg -o zoom_150/${base}.png ${OPTIONS} -w 24 -h 24
    inkscape ${base}.svg -o zoom_200/${base}.png ${OPTIONS} -w 32 -h 32
    inkscape ${base}.svg -o zoom_250/${base}.png ${OPTIONS} -w 40 -h 40
    inkscape ${base}.svg -o zoom_300/${base}.png ${OPTIONS} -w 48 -h 48
done
