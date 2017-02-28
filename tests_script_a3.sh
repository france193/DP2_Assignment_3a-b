#!/bin/bash

COUNTER=0

while [  $COUNTER -lt 20 ]; do
  echo " > Doing the $COUNTER execution..."

  time="$(date +%H%M%S)"

  ant run-tests -Dseed="$time" > ./tests/"$COUNTER"_test0_"$time"_dp2_a3.txt
  echo " > 1/1..."

  let COUNTER=COUNTER+1

  echo " > Done!"

done
