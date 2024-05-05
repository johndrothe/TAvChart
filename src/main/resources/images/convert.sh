# Converts the images in this folder to 16x16 PNGs using white as the transparency color

for file in `ls -1 *.svg`; do
    base=`basename ${file} .svg`
    convert -geometry 16x16 ${file} ${base}.png
    convert ${base}.png -transparent white ${base}.png
done
