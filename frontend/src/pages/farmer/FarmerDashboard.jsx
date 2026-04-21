import { useState, useEffect } from 'react';
import { useAuth } from '../../context/useAuth';
import { motion } from 'framer-motion';
import { Tractor, Calendar, Clock, CheckCircle, XCircle, MessageSquare } from 'lucide-react';
import { Link } from 'react-router-dom';
import api from '../../api/axios';
import ChatModal from '../../components/ChatModal';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer, Cell } from 'recharts';

const FarmerDashboard = () => {
    const { user } = useAuth();
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [selectedBooking, setSelectedBooking] = useState(null);
    const [isChatOpen, setIsChatOpen] = useState(false);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            const response = await api.get('/api/farmer/dashboard');
            setStats(response.data);
        } catch (err) {
            setError('Failed to fetch dashboard data');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const openChat = (booking) => {
        setSelectedBooking(booking);
        setIsChatOpen(true);
    };

    const chartData = stats?.bookings?.slice(0, 6).reverse().map((b, i) => ({
        name: b.equipment?.name?.split(' ')[0] || `B-${i + 1}`,
        amount: b.totalAmount,
    })) || [];

    const colors = ['#10b981', '#3b82f6', '#f59e0b', '#ef4444', '#8b5cf6', '#ec4899'];

    if (loading) return <div className="flex justify-center items-center h-64"><div className="animate-spin rounded-full h-12 w-12 border-b-2 border-primary-600"></div></div>;

    const getStatusBadge = (status) => {
        switch (status) {
            case 'PENDING': return <span className="flex items-center gap-1 text-orange-600 bg-orange-50 px-2 py-1 rounded text-xs font-medium"><Clock size={14} /> Pending</span>;
            case 'CONFIRMED': return <span className="flex items-center gap-1 text-green-600 bg-green-50 px-2 py-1 rounded text-xs font-medium"><CheckCircle size={14} /> Confirmed</span>;
            case 'COMPLETED': return <span className="flex items-center gap-1 text-blue-600 bg-blue-50 px-2 py-1 rounded text-xs font-medium"><CheckCircle size={14} /> Completed</span>;
            case 'CANCELLED': return <span className="flex items-center gap-1 text-red-600 bg-red-50 px-2 py-1 rounded text-xs font-medium"><XCircle size={14} /> Cancelled</span>;
            default: return status;
        }
    };

    return (
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
            <motion.div
                initial={{ opacity: 0, y: 20 }}
                animate={{ opacity: 1, y: 0 }}
            >
                <div className="flex justify-between items-center mb-8">
                    <div>
                        <h1 className="text-3xl font-bold text-gray-900">
                            Hello, {user?.fullName || 'Farmer'}
                        </h1>
                        <p className="text-gray-600 mt-1">Manage your equipment rentals and bookings</p>
                    </div>
                    <Link to="/equipment" className="bg-primary-600 text-white px-6 py-2 rounded-full hover:bg-primary-700 transition shadow-md flex items-center gap-2">
                        <Tractor size={20} /> Rent Equipment
                    </Link>
                </div>

                {/* Stats Overview */}
                <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-12">
                    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                        <div className="flex items-center gap-4">
                            <div className="p-3 bg-blue-50 rounded-lg">
                                <Calendar className="text-blue-600" size={24} />
                            </div>
                            <div>
                                <p className="text-sm text-gray-500">Active Bookings</p>
                                <p className="text-2xl font-bold text-gray-900">{stats?.activeBookings || 0}</p>
                            </div>
                        </div>
                    </div>
                </div>

                {/* Expenditure Chart */}
                <div className="bg-white p-8 rounded-3xl shadow-sm border border-gray-100 mb-12">
                    <h2 className="text-xl font-black text-gray-900 mb-8">My Performance Analytics</h2>
                    <div className="h-[250px] w-full">
                        {chartData.length > 0 ? (
                            <ResponsiveContainer width="100%" height="100%">
                                <BarChart data={chartData}>
                                    <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="#f1f5f9" />
                                    <XAxis dataKey="name" axisLine={false} tickLine={false} tick={{ fontSize: 12, fill: '#94a3b8' }} />
                                    <YAxis axisLine={false} tickLine={false} tick={{ fontSize: 12, fill: '#94a3b8' }} tickFormatter={(val) => `₹${val}`} />
                                    <Tooltip
                                        cursor={{ fill: '#f8fafc' }}
                                        contentStyle={{ backgroundColor: '#fff', borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)' }}
                                    />
                                    <Bar dataKey="amount" radius={[6, 6, 0, 0]} barSize={40}>
                                        {chartData.map((entry, index) => (
                                            <Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
                                        ))}
                                    </Bar>
                                </BarChart>
                            </ResponsiveContainer>
                        ) : (
                            <div className="h-full flex items-center justify-center text-gray-400 italic">Start renting to see your analytics</div>
                        )}
                    </div>
                </div>

                <h2 className="text-xl font-bold text-gray-900 mb-6">Recent Bookings</h2>
                <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                    {stats?.bookings && stats.bookings.length > 0 ? (
                        <div className="divide-y divide-gray-100">
                            {stats.bookings.map((booking) => (
                                <div key={booking.id} className="p-6 flex items-center justify-between hover:bg-gray-50 transition">
                                    <div className="flex items-center gap-4">
                                        <div className="h-12 w-12 rounded-lg bg-gray-100 flex items-center justify-center">
                                            <Tractor className="text-gray-400" size={24} />
                                        </div>
                                        <div>
                                            <h3 className="font-semibold text-gray-900">{booking.equipment?.name || 'Unknown Equipment'}</h3>
                                            <p className="text-sm text-gray-500">
                                                {booking.rentalType === 'HOURLY' ?
                                                    `${new Date(booking.startTime).toLocaleTimeString()} (${booking.totalDuration} hrs)` :
                                                    `${booking.startDate} to ${booking.endDate} (${booking.totalDuration} ${booking.rentalType === 'WEEKLY' ? 'weeks' : 'days'})`
                                                }
                                                <span className="mx-2">•</span>
                                                <span className="font-bold text-primary-700">₹{booking.totalAmount}</span>
                                                <span className="ml-2 bg-gray-100 px-2 py-0.5 rounded text-[10px] font-black uppercase tracking-widest">{booking.rentalType}</span>
                                            </p>
                                        </div>
                                    </div>
                                    <div className="flex items-center gap-4">
                                        <button
                                            onClick={() => openChat(booking)}
                                            className="p-2 text-gray-400 hover:text-primary-600 hover:bg-primary-50 rounded-full transition relative"
                                            title="Chat with Owner"
                                        >
                                            <MessageSquare size={20} />
                                        </button>
                                        {getStatusBadge(booking.status)}
                                    </div>
                                </div>
                            ))}
                        </div>
                    ) : (
                        <div className="p-12 text-center text-gray-500">
                            <Tractor className="mx-auto mb-4 text-gray-300" size={48} />
                            <p>No bookings found. Start by renting equipment!</p>
                        </div>
                    )}
                </div>

                {/* Chat Modal */}
                <ChatModal
                    isOpen={isChatOpen}
                    onClose={() => setIsChatOpen(false)}
                    booking={selectedBooking}
                />
            </motion.div>
        </div>
    );
};

export default FarmerDashboard;
