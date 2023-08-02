import {Link} from 'react-router-dom';
import axios from "axios";
import './Lobby2.css'
// import {useNavigate} from "react-router-dom"

// 대기방 - 픽토그램



const Lobby=()=> {
    
    //axios요청 => room에 5명있때만 게임 실행
    const gamestart = async () => {
        try {
            const requestData = {
                sessionId : "session_id_asd",
            };
            const response = await axios.post(
                "http://localhost:5000/api/room/start", 
                requestData,
            );
            console.log(response)
            // navigate(`/lobby/${value}`)
            
        } catch (error) {
            console.error("생성 실패", error);
        }
    };

    return (
        <div className='lobbymain'>
            <div className='lobbylogo'>로고</div>
            <div className='lobbyheader'>
                <div className='title1'>
                    <p>플레이할 게임</p>
                    <h1>점핑 잭으로 박터트리기!</h1>
                </div>
                <div className='title1'>
                    <p>초대코드</p>
                    <h1>~~~~~~~~</h1>
                </div>
                <div className='title1'>
                    <p>제한 시간</p>
                    <h1>1분 </h1>
                </div>
                
            </div>
            <div className='lobbybody'>
                <div className='waiting-list'>
                    참가자
                </div>
                <div className='select-type'>
                    <h1>현재 인원수</h1>
                    <h1>이모지 선택</h1>
                    <button onClick={gamestart}>+</button>
                    <Link to='/gamepage/1'><button>게임시작</button></Link>
                    <Link to='/main'><button>방나가기</button></Link>
                </div>
            </div>
            
        </div>
    )
}

export default Lobby