import '../assets/css/footer.css'
import home from '../assets/images/icons/home_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import person from '../assets/images/icons/person_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import more_horiz from '../assets/images/icons/more_horiz_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg'
import location from '../assets/images/icons/my_location_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg';
import celebration from '../assets/images/icons/celebration_24dp_1F1F1F_FILL0_wght400_GRAD0_opsz24.svg';


import { Link } from 'react-router-dom'

// Footer
export default function Footer() {
    return (<>
        <div id="footer">
            <Link to='/menu' className='menu'>
                <img src={more_horiz} className='icon' />
                <span> 카테고리 </span>
            </Link>
            <Link to='/menu' className='menu'>
                <img src={location} className='icon' />
                <span> 내주변 </span>
            </Link>
            <Link to='/' className='menu'>
                <img src={home} className='icon' />
                <span>홈</span>
            </Link>
            <Link to="/events" className="menu">
                <img src={celebration} className="icon" alt="이벤트" />
                <span>이벤트</span>
            </Link>
            <Link to='/mypage' className='menu'>
                <img src={person} className='icon' />
                <span>마이</span>
            </Link>
        </div>
    </>)
}