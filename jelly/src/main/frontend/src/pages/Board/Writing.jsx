import React, { useEffect, useState } from "react"
import axios from "axios"
import Styled from "styled-components"
import Header from "../../components/domain/Header"
import Navigation from "../../components/domain/Navigation"
import { useNavigate } from "react-router-dom"


const Writing = () => {

    const navigate = useNavigate();
    const handleButtonClick = () => {
        navigate("/board");
    };
    
    const [value, setValue] = useState('');
    const [boardTitle, setBoardTitle] = useState('')
    const [boardContent, setBoardContent] = useState('')
    const [memberAccount, setMemberAccount] = useState('pizzay@kakao.com')



    const handleTitleChange = (e) => {
        setBoardTitle(e.target.value);
    }

    const handleContentChange = (e) => {
        setBoardContent(e.target.value);
    }

    const handleAccountChange = (e) => {
        //이거 근데 필요한가요? 지금 저희는 Account가 고정값으로 들어가는데 음음음
        setMemberAccount(e.target.value);
    }

    const handleSubmit = (e) => {
        e.preventDefault();

        axios({
            url: "/board",
            method: "post",
            data: {
                boardTitle,
                boardContent,
                memberAccount
            }
        }).then((response) => {
            console.log(response)
        }).catch((error) => {
            console.log(error);
        });
    };

    return (
        <>
            <Header />
            <Navigation />
            <Topic>글 쓰기</Topic>
                <form onSubmit={handleSubmit}>
                <Title
                    type="text"
                    placeholder="*제목"
                    onChange={handleTitleChange}
                    value={boardTitle}
                />
                <Content
                    type="text"
                    placeholder="*글 내용"
                    onChange={handleContentChange}
                    value={boardContent}
                />
                <Button>
                    <Button1
                        type="submit"
                        onClick={handleButtonClick}
                    >    
                    제출 
                    </Button1>
                    <Button2 onClick={handleButtonClick}>취소하기</Button2>
                </Button>
            </form>
        </>
    );
}

const Topic = Styled.div`
    width: 100%;
    height: 3rem;
    background-color: #FFFFE0;
    text-align: center;
    font-size: 3rem;
    border: 3px solid #F5DA81;
    /* margin-top: 1rem; */
    /* 마진이 기준이 계속 바뀌는데 수정해야함 */
`

const Title = Styled.input`
    width: 100%;
    height: 4rem;
    border: 1px solid black;
    border-radius: 15px;
    margin-top: 1rem;
    background-color: #F4E6D0;
    font-size: 3rem;
`

const Content = Styled.input`
    width: 100%;
    height: 30rem;
    border: 1px solid black;
    border-radius: 15px;
    margin-top: 2rem;
    background-color: #F4E6D0;
    font-size: 3rem;
    /* 여기도 위에서 부터 시작하면 깔끔할 것 같은데 textarea align 다 해봤는데 좀 더 찾아서 수정해야함 */
`

const Button = Styled.div`
    float: right;
`

const Button1 = Styled.button`
    width: 5rem;
    height: 3rem;
    margin-top: 2rem;
    border-radius: 10px;
    border: 2px solid black;
    background-color: skyblue;
    &:hover{  
    background-color : #FFFFE0;
  }
    cursor: pointer;
    font-size: 2rem;
`
// #16f916 다른 버튼 색상
//#F5DA81

const Button2 = Styled.button`
    width: 5rem;
    height: 3rem;
    margin-top: 2rem;
    margin-left: 1rem;
    border-radius: 10px;
    border: 2px solid black;
    background-color: skyblue;
    &:hover{  
    background-color : #FFFFE0;
  }
    cursor: pointer;
    font-size: 2rem;
`
export default Writing;

