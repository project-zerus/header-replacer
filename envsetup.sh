#!/bin/usr/env bash

_find_blade_root ()
{
    local dir
    dir=$PWD;
    while [ "$dir" != "/" ]; do
        if [ -f "$dir/BLADE_ROOT" ]; then
            echo "$dir"
            return 0
        fi;
        dir=`dirname "$dir"`
    done
    return 1
}

export BLADE_ROOT=`_find_blade_root`

if [ $BLADE_ROOT ]; then
    export PATH=$BLADE_ROOT/tools/typhoon-blade:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/gyp:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/header-extractor/bin:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/wrk:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/ninja:$PATH
    source $BLADE_ROOT/tools/typhoon-blade/bladefunctions
fi;

maketools ()
{
    dir=`pwd`
    echo $dir
    cd $BLADE_ROOT/tools/devtools/wrk
    make
    cd $BLADE_ROOT/tools/devtools/ninja
    ./bootstrap.py
    cd $dir
}
