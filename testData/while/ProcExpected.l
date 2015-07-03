proc sum (a; b)
    if (a > b)
    then a := b;
    else b := a;
    fi
    b := b - a;
endp

while (5 > 5) do
  skip;
  if (x = 6)
  then x := 5;
  else y := 8;
  fi
od