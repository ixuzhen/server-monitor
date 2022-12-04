import HostInfo from "../compoent/Content/HostInfo";
import GPUHost from "../compoent/Content/GPUHost";
import {Navigate} from "react-router-dom";
import React from "react";
import GPUInfo from "../compoent/Content/GPUInfo";

export default [
    {
        path:'/hostinfo',
        element:<HostInfo/>
    },
    {
        path:'/gpuhost',
        element:<GPUHost/>,
        // children:[
        //     {
        //         path:'gpu/:ip',
        //         element:<div>123123333</div>
        //     }
        // ]
    },
    {
        path:'/gpuinfo/:ip',
        element:<GPUInfo/>
    },

    {
        path:'/',
        element:<Navigate to='/hostinfo'/>
    }
]