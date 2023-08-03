import React from 'react'
import SideNavBox from "../../components/MainPage/NavBox"
import MainSlide from "../../components/MainPage/Mainslide"
import './MainPage.css'

// 메인페이지

const Mainpage=(props)=> {

    return (
        <div>
            <div className='maintitle'>
                {/* 좌측 네비바 */}
                <div className='mainnavber'><SideNavBox/></div>
                {/* 우측 게임입장 슬라이드 */}
                <div className='maingame'><MainSlide/></div>
            </div>

        </div>
    )
}

export default Mainpage