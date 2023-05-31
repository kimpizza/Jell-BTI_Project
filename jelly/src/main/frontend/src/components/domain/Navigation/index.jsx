import Button from "components/base/Button"
// import useAuth from "hooks/useAuth"
import { useEffect } from "react"
import { Link, useNavigate } from "react-router-dom"
import styled from "styled-components"

const MenuBox = styled.ul`
    display: flex;
    justify-content: center;
`

const MenuList = styled.li`
    font-size: 48px;
    font-weight: 700;
    padding-left: 51px;
    padding: 20px 48px;
    cursor: pointer;
    :nth-child(1){
        color : #00A3FF;
    }
    a{
        text-decoration: none;
        color: inherit;
        display: block;
        border-radius: 30px;
        padding: 7px 20px;
    }
`

const Navigation = ()=>{
    const navigate = useNavigate()
    const Auth = (route)=>{
            const token = localStorage.getItem('token');
            if(token){
                navigate(`${route}`)
            }else{
                alert("로그인을 해주세요.")
            }
        }

    return(
        <MenuBox>
            <MenuList><Link to="/home">홈</Link></MenuList>
            <MenuList><Link to="">젤리 테스트</Link></MenuList>
            <MenuList><Link to="/board">커뮤니티</Link></MenuList>
            <MenuList onClick={()=>Auth("/mypage")}>마이페이지</MenuList>
        </MenuBox>
    )
}

export default Navigation