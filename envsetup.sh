#!/bin/usr/env bash

find_blade_root ()
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

BLADE_ROOT=`find_blade_root`

if [ $BLADE_ROOT ]; then
    echo "Set environment variable BLADE_ROOT $BLADE_ROOT"
    export BLADE_ROOT
    export PATH=$BLADE_ROOT/tools/typhoon-blade:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/gyp:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/header-extractor/bin:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/wrk:$PATH
    export PATH=$BLADE_ROOT/tools/devtools/ninja:$PATH
    source $BLADE_ROOT/tools/typhoon-blade/bladefunctions
else
    echo "Can't find the file 'BLADE_ROOT' in this or any upper directory."
    echo "Blade need this file as a placeholder to locate the root source directory "
    echo "(aka the directory where you #include start from)."
    echo "You should create it manually at the first time."
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
