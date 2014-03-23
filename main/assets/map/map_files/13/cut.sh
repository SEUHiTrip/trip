#!/bin/bash

validate_png(){
	identify $1 | grep PNG
	echo "identify" $?
	if [ "$?" -eq "1" ]; then
		echo "err:is not png"
		return 1
	else
		echo "png is validated"
		return 0
	fi
}

cut_main(){
	echo "cutting" $1
	width=$(identify -format "%w" $1)
	height=$(identify -format "%h" $1)
	let width_loop=width/255
	let height_loop=height/255
	echo $width_loop,$height_loop

	for (( i = 0; i < height_loop; i++ )); do
		for (( j = 0; j < width_loop; j++ )); do
			let h_offset=i*255
			let w_offset=j*255
			echo $h_offset,$w_offset
			`convert -crop 255x255+"$w_offset"+"$h_offset" dis.png "$j"_"$i".png`
		done
	done
}

cut_png_to_pieces(){
	validate_png $1
	echo "validate_png" $?
	if [ "$?" -eq "0" ]; then
		cut_main $1
	else 
		echo "exiting"
	fi
}

echo 'preparing to cut '$1', is that ok?(y)'
read ans
case $ans in 
	y|Y|yes|Yes)
		cut_png_to_pieces $1
esac